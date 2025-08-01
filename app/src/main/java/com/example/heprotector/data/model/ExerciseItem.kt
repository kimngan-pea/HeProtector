package com.example.heprotector.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_table")
data class ExerciseItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val duration: Int,
    val unit: String,
    val day: String,
    val userId: Int
)

