package com.example.heprotector.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val day: String, // "MO", "TU", ...
    val name: String,
    val kcal: Int,
    val unit: String = "kcal"
)
