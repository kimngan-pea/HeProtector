package com.example.heprotector_.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.heprotector_.data.db.dao.HealthRecordDao
import com.example.heprotector_.data.db.entities.Health_Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class HealthRecordRepository(private val healthRecordDao: HealthRecordDao) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun recordHealthData(
        userId: Int,
        bloodSugar: Double?,
        hba1c: Double?,
        weight: Double?,
        dateRecorded: LocalDateTime = LocalDateTime.now()
    ) = withContext(Dispatchers.IO) {
        val record = Health_Record(
            userId = userId,
            bloodSugar = bloodSugar,
            hba1c = hba1c,
            weight = weight,
            dateRecorded = dateRecorded
        )
        healthRecordDao.insertHealthRecord(record)
    }

    fun getHealthRecordsForUser(userId: Int): Flow<List<Health_Record>> {
        return healthRecordDao.getHealthRecordsForUser(userId)
    }

    fun getWeightHistoryForUser(userId: Int): Flow<List<Double>> {
        return healthRecordDao.getWeightHistoryForUser(userId)
    }

    suspend fun getLatestHealthRecordForUser(userId: Int): Health_Record? =
        withContext(Dispatchers.IO) {
            healthRecordDao.getLatestHealthRecordForUser(userId)
        }
}