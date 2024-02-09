package de.dercodeling.pizzacounter.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.domain.model.Setting

@Database(
    entities = [PizzaType::class, Setting::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class PizzaCounterDatabase: RoomDatabase() {
    abstract fun getDao(): PizzaCounterDao

    companion object {
        val migration2To3 = object : Migration(2,3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `Setting` (`key` TEXT NOT NULL, `value` TEXT NOT NULL, PRIMARY KEY(`key`))")
            }
        }

        @Volatile
        private var Instance: PizzaCounterDatabase? = null

        fun getDatabase(context: Context): PizzaCounterDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    PizzaCounterDatabase::class.java,
                    "pizza_list.db"
                )
                    .addMigrations(migration2To3)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}