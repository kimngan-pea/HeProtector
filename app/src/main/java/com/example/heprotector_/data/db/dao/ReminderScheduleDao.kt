package com.example.heprotector_.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.heprotector_.data.db.entities.Reminder_schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminderSchedule(reminder: Reminder_schedule)

    @Update
    suspend fun updateReminderSchedule(reminder: Reminder_schedule)

    @Delete
    suspend fun deleteReminderSchedule(reminder: Reminder_schedule)

    @Query("SELECT * FROM reminder_schedules WHERE userId = :userId AND isActive = 1 ORDER BY reminderTime ASC")
    fun getActiveRemindersForUser(userId: Int): Flow<List<Reminder_schedule>>

    @Query("SELECT * FROM reminder_schedules WHERE reminderId = :reminderId")
    suspend fun getReminderById(reminderId: Int): Reminder_schedule?
}