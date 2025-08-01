package com.example.heprotector.data.dao

import androidx.room.*
import com.example.heprotector.data.model.ReminderItem
import kotlinx.coroutines.flow.Flow
@Dao
interface ReminderDao {

    @Query("""
        SELECT * FROM reminders 
        WHERE userId = :userId AND type = :type AND day = :day
    """)
    fun getReminders(userId: Int, type: String, day: String): Flow<List<ReminderItem>>
    @Query("SELECT * FROM reminders WHERE userId = :userId")
    fun getReminders(userId: Int): Flow<List<ReminderItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderItem)

    @Update
    suspend fun updateReminder(reminder: ReminderItem)

    @Delete
    suspend fun deleteReminder(reminder: ReminderItem)

}


