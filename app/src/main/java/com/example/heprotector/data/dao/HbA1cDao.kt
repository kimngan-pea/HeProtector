package com.example.heprotector.data.dao

import androidx.room.*
import com.example.heprotector.data.model.HbA1c
import kotlinx.coroutines.flow.Flow

@Dao
interface HbA1cDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: HbA1c)

    @Query("SELECT * FROM hba1c WHERE userId = :userId ORDER BY time DESC")
    fun getByUserId(userId: Int): Flow<List<HbA1c>>
}
