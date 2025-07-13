package com.example.heprotector_.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.heprotector_.data.db.entities.Medication_schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicationSchedule(schedule: Medication_schedule)

    @Update
    suspend fun updateMedicationSchedule(schedule: Medication_schedule)

    @Delete
    suspend fun deleteMedicationSchedule(schedule: Medication_schedule)

    @Query("SELECT * FROM medication_schedules WHERE userId = :userId ORDER BY scheduleTime DESC")
    fun getMedicationSchedulesForUser(userId: Int): Flow<List<Medication_schedule>>
}