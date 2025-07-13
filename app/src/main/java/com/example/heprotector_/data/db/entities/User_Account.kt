package com.example.heprotector_.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_accounts")
data class User_Account(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val username: String,
    val email: String,
    val passwordHash: String, // Store password securely, never plain text
    val height: Double? // Nullable if height is not mandatory at registration
)