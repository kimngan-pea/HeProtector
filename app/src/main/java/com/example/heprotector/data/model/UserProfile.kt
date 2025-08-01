package com.example.heprotector.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val userId: Int,
    val username: String = "",
    val fullName: String? = "",
    val email: String? = "",
    val address: String? = "",
    val management: String? = "",
    val dateOfBirth: Long? = null,
    val gender: String? = "",
    val height: Float? = null, // cm
    val weight: Float? = null, // kg
    val diabetesType: String? = "",
    val diagnosisMonth: Int? = null,
    val diagnosisYear: Int? = null,
    val goalWeight: Float? = null
)
