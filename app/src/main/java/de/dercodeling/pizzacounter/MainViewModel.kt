package de.dercodeling.pizzacounter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class ThemeSetting(
    val isFollowSystem: Boolean = true,
    val isDark: Boolean = false
)

class MainViewModel(
    private val dao: PizzaTypeDao
) : ViewModel() {
    private var themeSetting by mutableStateOf(ThemeSetting())

    private var pizzasMap by mutableStateOf(mutableMapOf<String, Int>())

    @set:JvmName("setInitialTypesList") // Needed to avoid conflict of automatically generated setter name and manually defined one
    private var initialTypes by mutableStateOf(
        listOf(
            "Margherita",
            "Prosciutto",
            "Salami"/*,
            "Tonno e cipolla",
            "Regina",
            "Dalla casa",
            "Arrabiata",
            "Funghi",
            "Quattro stagioni"*/
        )
    )

    private var sortingBy by mutableStateOf(SortType.NAME)

    // THEME

    fun getTheme(): ThemeSetting {
        return themeSetting
    }

    fun setTheme(
        isFollowSystem: Boolean,
        isDark: Boolean
    ) {
        themeSetting = ThemeSetting(isFollowSystem = isFollowSystem, isDark = isDark)
    }

    // INITIAL TYPES
    fun setInitialTypes(newTypesList: List<String>) {
        if(newTypesList.isNotEmpty()) initialTypes = newTypesList
    }

    // MAP LOGIC

    fun getSize(): Int {
        return pizzasMap.size
    }

    fun getTypes(): MutableList<String> {
        var keys = mutableListOf<String>()

        // Add keys to List according to sorting setting

        when(sortingBy) {
            SortType.NAME -> keys = pizzasMap.toSortedMap().keys.toList().toMutableList()

            SortType.QUANTITY_ASC -> {
                var pizzasList = pizzasMap.toList()

                pizzasList = pizzasList.sortedWith(
                    compareBy(
                        { (_, value) -> value },
                        { (key, _) -> key }
                    )
                )

                for (pizza in pizzasList) {
                    keys.add(pizza.first)
                }
            }

            SortType.QUANTITY_DESC -> {
                var pizzasList = pizzasMap.toList()

                pizzasList = pizzasList.sortedWith(
                    compareByDescending<Pair<String, Int>> { (_, value) -> value }
                        .thenBy { (key, _) -> key }
                )

                for (pizza in pizzasList) {
                    keys.add(pizza.first)
                }
            }
        }

        return keys
    }

    fun getQuantity(name: String): Int {
        return if (pizzasMap[name] != null) pizzasMap[name]!! else 0
    }

    fun addType(name: String) {
        pizzasMap[name] = 0

        viewModelScope.launch {
            dao.upsertPizzaType(PizzaType(name,0))
        }
    }

    fun changeQuantity(name: String, change: Int) {
        val current = pizzasMap[name]

        if (current != null) {
            var newQuantity = current.plus(change)

            if (newQuantity < 0) newQuantity = 0

            // Setting the quantity directly with pizzasMap[type]=newQuantity ist not sufficient
            // to trigger reloading of UI, a new Map has to be created as follows:
            val newPizzasMap = pizzasMap.toSortedMap()
            newPizzasMap[name] = newQuantity
            pizzasMap = newPizzasMap
        } else {
            throw NullPointerException("Type provided to changeQuantity does not exist, cannot change quantity. ")
        }
    }

    // SORTING

    fun getSortBy(): SortType {
        return sortingBy
    }

    fun setSortBy(newSortBy: SortType) {
        sortingBy = newSortBy
    }

    // CLEARING

    fun clearQuantities() {
        pizzasMap.forEach { entry -> pizzasMap[entry.key] = 0 }
    }

    fun clearTypes() {
        pizzasMap = mutableMapOf()

        for(type in initialTypes){
            addType(type)
        }
    }
}