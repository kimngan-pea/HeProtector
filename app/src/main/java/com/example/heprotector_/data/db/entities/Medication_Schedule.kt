package com.example.heprotector_.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "medication_schedules",
    foreignKeys = [ForeignKey(entity = User_Account::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE)])
data class Medication_schedule(
    @PrimaryKey(autoGenerate = true) val scheduleId: Int = 0,
    val userId: Int,
    val medicationName: String,
    val imageUri: String?, // Store URI to local image, or path
    val dosage: Int,
    val scheduleTime: LocalDateTime // Time of day, potentially with a date
)