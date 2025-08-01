package com.example.heprotector.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class MedicationSchedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val medicine: String,
    val dosage: String,
    val hour: Int,
    val minute: Int,
    val day: String // NEW:  "MO", "TU", "WE"
)
