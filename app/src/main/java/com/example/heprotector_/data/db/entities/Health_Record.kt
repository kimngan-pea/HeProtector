package com.example.heprotector_.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "health_records",
    foreignKeys = [ForeignKey(entity = User_Account::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE)])
data class Health_Record(
    @PrimaryKey(autoGenerate = true) val recordId: Int = 0,
    val userId: Int,
    val bloodSugar: Double?, // Nullable if user doesn't track this
    val hba1c: Double?,      // Nullable
    val weight: Double?,     // Nullable
    val dateRecorded: LocalDateTime // Use LocalDateTime and a TypeConverter
)