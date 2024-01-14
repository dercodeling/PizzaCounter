package de.dercodeling.pizzacounter

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PizzaTypeDao {
    // TODO: Add functions for resetting quantities to 0 and clearing table

    @Upsert
    suspend fun upsertPizzaType(pizzaType: PizzaType)

    @Delete
    suspend fun deletePizzaType(pizzaType: PizzaType)

    @Query("SELECT * FROM pizzaType ORDER BY name ASC")
    fun getPizzaTypesOrderedByName(): Flow<List<PizzaType>>

    @Query("SELECT * FROM pizzaType ORDER BY quantity ASC")
    fun getPizzaTypesOrderedByQuantityAscending(): Flow<List<PizzaType>>

    @Query("SELECT * FROM pizzaType ORDER BY quantity DESC")
    fun getPizzaTypesOrderedByQuantityDescending(): Flow<List<PizzaType>>
}