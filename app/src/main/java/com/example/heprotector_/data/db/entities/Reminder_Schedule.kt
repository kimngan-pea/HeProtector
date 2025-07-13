package com.example.heprotector_.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "reminder_schedules",
    foreignKeys = [ForeignKey(entity = User_Account::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE)])
data class Reminder_schedule(
    @PrimaryKey(autoGenerate = true) val reminderId: Int = 0,
    val userId: Int,
    val reminderType: String, // e.g., "Blood Sugar", "Weight", "Medication"
    val reminderTime: LocalTime, // Time of day for the reminder
    val isActive: Boolean = true // To enable/disable reminders
)