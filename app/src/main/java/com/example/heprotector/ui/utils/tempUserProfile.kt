package com.example.heprotector.ui.utils

import com.example.heprotector.data.model.UserProfile

object TempUserProfile {
    var username: String = ""
    var fullName: String = ""
    var location: String = ""
    var management: String = ""
    var dateOfBirth: Long = 0L

    var gender: String = ""
    var height: String = ""
    var weight: String = ""

    var diabetesType: String = ""
    var diagnosisMonth: String = ""
    var diagnosisYear: String = ""

    var goalWeight: String = ""


    fun toUserProfile(userId: Int): UserProfile {
        return UserProfile(
            userId = userId,
            username = username,
            fullName = fullName,
            address = location,
            management = management,
            dateOfBirth = dateOfBirth,
            gender = gender,
            height = height.toFloatOrNull() ?: 0f,
            weight = weight.toFloatOrNull() ?: 0f,
            diabetesType = diabetesType,
            diagnosisMonth = diagnosisMonth.toIntOrNull() ?: 0,
            diagnosisYear = diagnosisYear.toIntOrNull() ?: 0,
            goalWeight = goalWeight.toFloatOrNull() ?: 0f
        )
    }
}
