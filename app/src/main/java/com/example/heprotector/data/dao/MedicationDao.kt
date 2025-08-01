package com.example.heprotector.data.dao

import androidx.room.*
import com.example.heprotector.data.model.MedicationSchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications WHERE userId = :userId AND day = :day")
    fun getMedicationsByUserAndDay(userId: Int, day: String): Flow<List<MedicationSchedule>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medication: MedicationSchedule)

    @Update
    suspend fun update(medication: MedicationSchedule)

    @Delete
    suspend fun delete(medication: MedicationSchedule)
}

