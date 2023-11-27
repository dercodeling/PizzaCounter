package de.dercodeling.pizzacounter

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var pizzasState = mutableStateMapOf<String, Int>()

    fun getSize() : Int{
        return pizzasState.size
    }
    fun getType(index: Int) : String {
        return pizzasState.keys.elementAt(index)
    }
    fun getQuantity(type: String) : Int? {
        return pizzasState[type]
    }
    fun changeQuantity(type: String, change: Int){
        val current = pizzasState[type]
        val new = current?.plus(change)
        if (new != null) {
            if(new>=0) pizzasState[type] = new
        }
    }
    fun addType(type: String) {
        pizzasState[type] = 0
    }
}