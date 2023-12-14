package de.dercodeling.pizzacounter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class ThemeSetting(
    val isFollowSystem: Boolean = true,
    val isDark: Boolean = false
)

class MainViewModel : ViewModel() {
    private var themeSetting by mutableStateOf(ThemeSetting())

    private var pizzasMap by mutableStateOf(mutableMapOf<String, Int>())

    private var sortingBy by mutableStateOf(0)

    // THEME

    fun getTheme() : ThemeSetting {
        return themeSetting
    }

    fun setTheme(
        isFollowSystem: Boolean,
        isDark: Boolean
    ){
        themeSetting = ThemeSetting(isFollowSystem = isFollowSystem, isDark = isDark)
    }

    // LIST LOGIC

    fun getSize() : Int{
        return pizzasMap.size
    }
    fun getTypes() : MutableList<String> {
        var keys = mutableListOf<String>()

        // Add keys to List according to sorting setting

        if(sortingBy>0){ // Sort by quantity
            var pizzasList = pizzasMap.toList()

            // Quantity (ascending)
            if(sortingBy == 1) pizzasList = pizzasList.sortedWith(
                compareBy(
                    { (_,value) -> value },
                    { (key,_) -> key }
                )
            )

            // Quantity (descending)
            if(sortingBy == 2) pizzasList = pizzasList.sortedWith(
                compareByDescending<Pair<String,Int>>{ (_,value) -> value }
                    .thenBy { (key,_) -> key }
            )

            for(pizza in pizzasList){
                keys.add(pizza.first)
            }

        }else { // Sort by type
            keys = pizzasMap.toSortedMap().keys.toList().toMutableList()
        }

        return keys
    }
    fun getQuantity(type: String) : Int? {
        return pizzasMap[type]
    }

    fun addType(type: String) {
        pizzasMap[type] = 0
    }
    fun changeQuantity(type: String, change: Int){
        val current = pizzasMap[type]
        val newQuantity = current?.plus(change)

        if (newQuantity != null) {
            if(newQuantity>=0) {
                // Setting the quantity directly with pizzasMap[type]=newQuantity ist not sufficient to trigger reloading of UI,
                // a new Map has to be created as follows:
                val newPizzasMap = pizzasMap.toSortedMap()
                newPizzasMap[type] = newQuantity
                pizzasMap = newPizzasMap
            }
        }
    }

    // SORTING

    fun getSortBy() : Int{
        return sortingBy
    }
    fun setSortBy(newSortBy: Int){
        sortingBy = newSortBy
    }
}