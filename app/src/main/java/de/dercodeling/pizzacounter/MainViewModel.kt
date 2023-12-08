package de.dercodeling.pizzacounter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    //var pizzasMap = mutableMapOf<String, Int>()
    var pizzasMap by mutableStateOf(sortedMapOf<String, Int>())

    fun getSize() : Int{
        return pizzasMap.size
    }
    fun getType(index: Int) : String {
        return pizzasMap.keys.elementAt(index)
    }
    fun getQuantity(type: String) : Int? {
        return pizzasMap[type]
    }
    fun changeQuantity(type: String, change: Int){
        val current = pizzasMap[type]
        val newQuantity = current?.plus(change)
        if (newQuantity != null) {
            if(newQuantity>=0) pizzasMap[type] = newQuantity
        }
    }
    fun addType(type: String) {
        pizzasMap[type] = 0
    }

    fun sortBy(sortBy: String){
        // TODO: Implement sorting
        //pizzasMap.toSortedMap()
    }
}