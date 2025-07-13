package com.example.heprotector_.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heprotector_.data.db.entities.Local_Food
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalFoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalFood(food: Local_Food)

    @Query("SELECT * FROM local_foods ORDER BY foodName ASC")
    fun getAllLocalFoods(): Flow<List<Local_Food>>

    @Query("SELECT * FROM local_foods WHERE foodName LIKE '%' || :query || '%'")
    fun searchLocalFoods(query: String): Flow<List<Local_Food>>
}