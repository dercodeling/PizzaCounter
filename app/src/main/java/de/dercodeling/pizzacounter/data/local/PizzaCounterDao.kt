package de.dercodeling.pizzacounter.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.domain.model.Setting
import kotlinx.coroutines.flow.Flow

@Dao
interface PizzaCounterDao {
    // Table: pizzaType
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPizzaType(pizzaType: PizzaType)

    @Update
    suspend fun updatePizzaType(pizzaType: PizzaType)

    @Delete
    suspend fun deletePizzaType(pizzaType: PizzaType)

    @Query("SELECT * FROM pizzaType ORDER BY name ASC")
    fun getPizzaTypesOrderedByName(): Flow<List<PizzaType>>

    @Query("SELECT * FROM pizzaType ORDER BY quantity ASC")
    fun getPizzaTypesOrderedByQuantityAscending(): Flow<List<PizzaType>>

    @Query("SELECT * FROM pizzaType ORDER BY quantity DESC")
    fun getPizzaTypesOrderedByQuantityDescending(): Flow<List<PizzaType>>

    @Query("UPDATE pizzatype SET quantity = 0")
    suspend fun clearQuantities()

    @Query("DELETE FROM pizzatype")
    suspend fun clearTypes()

    // Table: setting

    @Upsert
    suspend fun upsertSetting(setting: Setting)

    @Query("SELECT * FROM setting WHERE `key`=:key")
    fun getSettingByKey(key: String): Flow<Setting>
}