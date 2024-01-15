package de.dercodeling.pizzacounter

import androidx.room.Database
import androidx.room.RoomDatabase
import de.dercodeling.pizzacounter.model.PizzaType

@Database(
    entities = [PizzaType::class],
    version = 1
)
abstract class PizzaListDatabase: RoomDatabase() {

    abstract val dao: PizzaTypeDao
}