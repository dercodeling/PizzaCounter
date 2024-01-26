package de.dercodeling.pizzacounter.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.dercodeling.pizzacounter.domain.model.PizzaType

@Database(
    entities = [PizzaType::class],
    version = 1
)
abstract class PizzaCounterDatabase: RoomDatabase() {
    abstract fun getDao(): PizzaTypeDao

    companion object {
        @Volatile
        private var Instance: PizzaCounterDatabase? = null

        fun getDatabase(context: Context): PizzaCounterDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    PizzaCounterDatabase::class.java,
                    "pizza_list.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}