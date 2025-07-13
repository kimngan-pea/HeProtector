package com.example.heprotector_.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heprotector_.data.db.entities.Health_Record
import com.example.heprotector_.data.repository.HealthRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlinx.coroutines.flow.SharingStarted

class HealthRecordViewModel(
    private val healthRecordRepository: HealthRecordRepository,
    private val sharedViewModel: SharedViewModel // To get current user ID
) : ViewModel() {

    private val _bloodSugar = MutableStateFlow("")
    val bloodSugar: StateFlow<String> = _bloodSugar.asStateFlow()

    private val _hba1c = MutableStateFlow("")
    val hba1c: StateFlow<String> = _hba1c.asStateFlow()

    private val _weight = MutableStateFlow("")
    val weight: StateFlow<String> = _weight.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun onBloodSugarChange(value: String) { _bloodSugar.value = value }
    fun onHba1cChange(value: String) { _hba1c.value = value }
    fun onWeightChange(value: String) { _weight.value = value }
    fun dismissMessage() { _message.value = null }


    // Combine flows to get health records for the current user
    val healthRecords: StateFlow<List<Health_Record>> =
        sharedViewModel.currentUserId
            .combine(healthRecordRepository.getHealthRecordsForUser(sharedViewModel.currentUserId.value ?: -1)) { userId, records ->
                // Filter records by current user ID if needed, or simply pass the flow directly
                // The filter is important if the DAO returns all records and not just for the user.
                // If your getHealthRecordsForUser already filters by userId, this filter might be redundant but harmless.
                records.filter { it.userId == userId }
            }
            .stateIn( // <--- Use stateIn here!
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000), // Start collecting when there are subscribers, stop after 5s inactivity
                initialValue = emptyList() // Provide an initial value
            )


    @RequiresApi(Build.VERSION_CODES.O)
    fun recordHealthData() {
        viewModelScope.launch {
            val userId = sharedViewModel.currentUserId.value
            if (userId == null) {
                _message.value = "User not logged in."
                return@launch
            }

            val bsValue = _bloodSugar.value.toDoubleOrNull()
            val hba1cValue = _hba1c.value.toDoubleOrNull()
            val weightValue = _weight.value.toDoubleOrNull()

            if (bsValue == null && hba1cValue == null && weightValue == null) {
                _message.value = "Please enter at least one health metric."
                return@launch
            }

            try {
                healthRecordRepository.recordHealthData(
                    userId = userId,
                    bloodSugar = bsValue,
                    hba1c = hba1cValue,
                    weight = weightValue,
                    dateRecorded = LocalDateTime.now()
                )
                _message.value = "Health data recorded successfully!"
                // Clear inputs after successful record
                _bloodSugar.value = ""
                _hba1c.value = ""
                _weight.value = ""
            } catch (e: Exception) {
                _message.value = "Error recording data: ${e.message}"
            }
        }
    }

    // Function to calculate BMI (you might move this to a utility or domain layer)
    fun calculateBMI(weightKg: Double?, heightCm: Double?): Double? {
        if (weightKg == null || heightCm == null || heightCm == 0.0) return null
        val heightMeters = heightCm / 100.0
        return weightKg / (heightMeters * heightMeters)
    }
}

