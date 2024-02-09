package de.dercodeling.pizzacounter

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.dercodeling.pizzacounter.data.local.PizzaCounterDatabase

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

private const val DB_NAME = "test"

@RunWith(AndroidJUnit4::class)
class RoomMigrationTest {
    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        PizzaCounterDatabase::class.java,
        listOf(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migration1To2_containsCorrectData() {
        @Suppress("VARIABLE_WITH_REDUNDANT_INITIALIZER") // helper.createDatabase is needed
        var db = helper.createDatabase(DB_NAME, 1).apply {
            execSQL("INSERT INTO pizzaType VALUES('Margherita',4)")
            close()
        }

        db = helper.runMigrationsAndValidate(DB_NAME, 2, true)

        db.query("SELECT * FROM pizzaType").apply {
            assertEquals(true, moveToFirst())
            assertEquals("", getString(getColumnIndex("notes")))
        }
    }

    @Test
    fun testAllMigrations() {
        helper.createDatabase(DB_NAME, 1).apply { close() }

        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            PizzaCounterDatabase::class.java,
            DB_NAME
        )
            .addMigrations(PizzaCounterDatabase.migration2To3)
            .build()
            .apply {
                openHelper.writableDatabase.close()
            }
    }
}