package com.example.heprotector_.data.repository

import com.example.heprotector_.data.db.dao.ReminderScheduleDao
import com.example.heprotector_.data.db.entities.Reminder_schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ReminderScheduleRepository(private val reminderScheduleDao: ReminderScheduleDao) {

    /**
     * Inserts a new reminder schedule or updates an existing one.
     * @param reminder The Reminder_schedule object to insert/update.
     * @return The row ID of the newly inserted or updated reminder.
     */
//    suspend fun insertReminderSchedule(reminder: Reminder_schedule): Long =
//        withContext(Dispatchers.IO) {
//            reminderScheduleDao.insertReminderSchedule(reminder)
//        }

    /**
     * Updates an existing reminder schedule.
     * @param reminder The Reminder_schedule object to update.
     */
    suspend fun updateReminderSchedule(reminder: Reminder_schedule) =
        withContext(Dispatchers.IO) {
            reminderScheduleDao.updateReminderSchedule(reminder)
        }

    /**
     * Deletes a reminder schedule.
     * @param reminder The Reminder_schedule object to delete.
     */
    suspend fun deleteReminderSchedule(reminder: Reminder_schedule) =
        withContext(Dispatchers.IO) {
            reminderScheduleDao.deleteReminderSchedule(reminder)
        }

    /**
     * Retrieves all active reminder schedules for a specific user, ordered by reminder time.
     * @param userId The ID of the user.
     * @return A Flow emitting a list of active Reminder_schedule objects.
     */
    fun getActiveRemindersForUser(userId: Int): Flow<List<Reminder_schedule>> {
        return reminderScheduleDao.getActiveRemindersForUser(userId)
    }

    /**
     * Retrieves a specific reminder by its ID.
     * @param reminderId The ID of the reminder.
     * @return The Reminder_schedule object, or null if not found.
     */
    suspend fun getReminderById(reminderId: Int): Reminder_schedule? =
        withContext(Dispatchers.IO) {
            reminderScheduleDao.getReminderById(reminderId)
        }
}