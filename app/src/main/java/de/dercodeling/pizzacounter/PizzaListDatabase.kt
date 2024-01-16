package de.dercodeling.pizzacounter

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.dercodeling.pizzacounter.model.PizzaType

@Database(
    entities = [PizzaType::class],
    version = 1
)
abstract class PizzaListDatabase: RoomDatabase() {
    abstract fun getDao(): PizzaTypeDao

    companion object {
        @Volatile
        private var Instance: PizzaListDatabase? = null

        fun getDatabase(context: Context): PizzaListDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    PizzaListDatabase::class.java,
                    "pizza_list.db"
                )
                    .createFromAsset("pizza_list_initial.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}