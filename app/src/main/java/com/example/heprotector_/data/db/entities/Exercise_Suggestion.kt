package com.example.heprotector_.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "exercise_suggestions",
    foreignKeys = [ForeignKey(entity = Health_Record::class,
        parentColumns = ["recordId"],
        childColumns = ["recordId"],
        onDelete = ForeignKey.CASCADE)])
data class Exercise_Suggestion(
    @PrimaryKey(autoGenerate = true) val exerciseId: Int = 0,
    val recordId: Int, // Links to a specific health record if advice is based on it
    val name: String,
    val description: String,
    val suggestedDate: LocalDateTime // When this advice was suggested
)