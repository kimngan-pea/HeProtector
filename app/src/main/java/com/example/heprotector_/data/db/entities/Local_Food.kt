package com.example.heprotector_.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_foods")
data class Local_Food(
    @PrimaryKey(autoGenerate = true) val foodId: Int = 0,
    val foodName: String,
    val nutritionalInfo: String // Store as JSON string, or a simple string, or separate columns
)