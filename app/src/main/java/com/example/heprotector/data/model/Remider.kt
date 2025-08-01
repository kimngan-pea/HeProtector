package com.example.heprotector.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val type: String,             // "Blood Sugar", "Weight", "HbA1c"
    val day: String,              // "MO", "TU", "WE", etc.
    val period: String,           // "Morning", "Afternoon", etc.
    val beforeAfter: String,      // "Before", "After"
    val hour: Int,
    val minute: Int
)
