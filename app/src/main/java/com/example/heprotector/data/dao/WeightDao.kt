package com.example.heprotector.data.dao

import androidx.room.*
import com.example.heprotector.data.model.Weight
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weight: Weight)

    @Delete
    suspend fun delete(weight: Weight)

    @Query("DELETE FROM Weight")
    suspend fun deleteAll()

    @Query("SELECT * FROM Weight ORDER BY time ASC")
    fun getAll(): Flow<List<Weight>>

    @Query("SELECT * FROM Weight WHERE userId = :userId ORDER BY time DESC")
    fun getWeightsByUser(userId: Int): Flow<List<Weight>>


}
