package com.example.heprotector_.data.repository

import com.example.heprotector_.data.db.dao.MedicationScheduleDao
import com.example.heprotector_.data.db.entities.Medication_schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MedicationScheduleRepository(private val medicationScheduleDao: MedicationScheduleDao) {

    /**
     * Inserts a new medication schedule or updates an existing one.
     * @param schedule The Medication_schedule object to insert/update.
     */
    suspend fun insertMedicationSchedule(schedule: Medication_schedule) =
        withContext(Dispatchers.IO) {
            medicationScheduleDao.insertMedicationSchedule(schedule)
        }

    /**
     * Updates an existing medication schedule.
     * @param schedule The Medication_schedule object to update.
     */
    suspend fun updateMedicationSchedule(schedule: Medication_schedule) =
        withContext(Dispatchers.IO) {
            medicationScheduleDao.updateMedicationSchedule(schedule)
        }

    /**
     * Deletes a medication schedule.
     * @param schedule The Medication_schedule object to delete.
     */
    suspend fun deleteMedicationSchedule(schedule: Medication_schedule) =
        withContext(Dispatchers.IO) {
            medicationScheduleDao.deleteMedicationSchedule(schedule)
        }

    /**
     * Retrieves all medication schedules for a specific user, ordered by schedule time.
     * @param userId The ID of the user.
     * @return A Flow emitting a list of Medication_schedule objects.
     */
    fun getMedicationSchedulesForUser(userId: Int): Flow<List<Medication_schedule>> {
        return medicationScheduleDao.getMedicationSchedulesForUser(userId)
    }
}