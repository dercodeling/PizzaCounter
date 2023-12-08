package de.dercodeling.pizzacounter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var pizzasMap by mutableStateOf(mutableMapOf<String, Int>())

    private var sortingBy by mutableStateOf(0)

    fun getSize() : Int{
        return pizzasMap.size
    }
    fun getTypes() : MutableList<String> {
        var keys = mutableListOf<String>()

        if(sortingBy>0){
            var pizzasList = pizzasMap.toList().sortedBy { (_, value) -> value }

            if(sortingBy == 2) pizzasList = pizzasList.reversed()

            for(pizza in pizzasList){
                keys.add(pizza.first)
            }
        }else {
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

    fun getSortBy() : Int{
        return sortingBy
    }
    fun setSortBy(newSortBy: Int){
        sortingBy = newSortBy
    }
}