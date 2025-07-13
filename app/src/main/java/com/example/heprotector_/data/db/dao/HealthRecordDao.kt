package com.example.heprotector_.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heprotector_.data.db.entities.Health_Record
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthRecord(record: Health_Record)

    @Query("SELECT * FROM health_records WHERE userId = :userId ORDER BY dateRecorded DESC")
    fun getHealthRecordsForUser(userId: Int): Flow<List<Health_Record>> // Observe changes with Flow

    @Query("SELECT weight FROM health_records WHERE userId = :userId ORDER BY dateRecorded DESC")
    fun getWeightHistoryForUser(userId: Int): Flow<List<Double>>

    @Query("SELECT * FROM health_records WHERE userId = :userId ORDER BY dateRecorded DESC LIMIT 1")
    suspend fun getLatestHealthRecordForUser(userId: Int): Health_Record?
}