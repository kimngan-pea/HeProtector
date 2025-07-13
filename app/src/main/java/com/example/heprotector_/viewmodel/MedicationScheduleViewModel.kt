package com.example.heprotector_.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heprotector_.data.db.entities.Medication_schedule
import com.example.heprotector_.data.repository.MedicationScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.time.LocalDateTime // For scheduleTime
import java.time.LocalTime // For time pickers

class MedicationScheduleViewModel(
    private val medicationScheduleRepository: MedicationScheduleRepository,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {

    // Input fields for adding/editing a medication
    private val _medicationName = MutableStateFlow("")
    val medicationName: StateFlow<String> = _medicationName.asStateFlow()

    private val _dosage = MutableStateFlow("") // Represent as String for input
    val dosage: StateFlow<String> = _dosage.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _selectedTime = MutableStateFlow(LocalTime.now()) // For time picker
    @RequiresApi(Build.VERSION_CODES.O)
    val selectedTime: StateFlow<LocalTime> = _selectedTime.asStateFlow()

    private val _imageUri = MutableStateFlow<String?>(null) // URI string for an image
    val imageUri: StateFlow<String?> = _imageUri.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    // State for the list of medications
    val medicationSchedules: StateFlow<List<Medication_schedule>> =
        sharedViewModel.currentUserId
            .filterNotNull() // Only proceed if a user is logged in
            .combine(medicationScheduleRepository.getMedicationSchedulesForUser(sharedViewModel.currentUserId.value ?: -1)) { userId, schedules ->
                schedules.filter { it.userId == userId } // Ensure filtering by current user
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


    fun onMedicationNameChange(name: String) { _medicationName.value = name }
    fun onDosageChange(value: String) { _dosage.value = value }
    @RequiresApi(Build.VERSION_CODES.O)
    fun onTimeSelected(time: LocalTime) { _selectedTime.value = time }
    fun onImageUriSelected(uri: String?) { _imageUri.value = uri }
    fun dismissMessage() { _message.value = null }

    @RequiresApi(Build.VERSION_CODES.O)
    fun recordMedicationSchedule() {
        viewModelScope.launch {
            val userId = sharedViewModel.currentUserId.value
            if (userId == null) {
                _message.value = "User not logged in."
                return@launch
            }

            val name = _medicationName.value.trim()
            val dosageValue = _dosage.value.toIntOrNull()

            if (name.isEmpty() || dosageValue == null) {
                _message.value = "Please enter medication name and dosage."
                return@launch
            }

            try {
                // Combine selected time with today's date for a LocalDateTime
                // For recurring schedules, you'd need more complex logic (e.g., repeating daily/weekly)
                val scheduleDateTime = LocalDateTime.now()
                    .withHour(_selectedTime.value.hour)
                    .withMinute(_selectedTime.value.minute)
                    .withSecond(0)
                    .withNano(0)

                val newSchedule = Medication_schedule(
                    userId = userId,
                    medicationName = name,
                    imageUri = _imageUri.value,
                    dosage = dosageValue,
                    scheduleTime = scheduleDateTime
                )
                medicationScheduleRepository.insertMedicationSchedule(newSchedule)
                _message.value = "Medication schedule added!"
                clearInputs()
            } catch (e: Exception) {
                _message.value = "Error adding medication: ${e.localizedMessage}"
            }
        }
    }

    fun deleteMedicationSchedule(schedule: Medication_schedule) {
        viewModelScope.launch {
            try {
                medicationScheduleRepository.deleteMedicationSchedule(schedule)
                _message.value = "Medication schedule deleted."
            } catch (e: Exception) {
                _message.value = "Error deleting schedule: ${e.localizedMessage}"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun clearInputs() {
        _medicationName.value = ""
        _dosage.value = ""
        _selectedTime.value = LocalTime.now() // Reset to current time
        _imageUri.value = null
    }

    // You might add functions here to "monitor" by marking a dose as taken
    // For that, you'd need a field in Medication_schedule (e.g., `isTaken: Boolean`)
    // and an `updateMedicationSchedule` call.
}