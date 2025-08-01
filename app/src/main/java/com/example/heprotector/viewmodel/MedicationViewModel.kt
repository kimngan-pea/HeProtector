package com.example.heprotector.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heprotector.data.dao.MedicationDao
import com.example.heprotector.data.model.MedicationSchedule
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MedicationViewModel(
    private val dao: MedicationDao,
    private val userId: Int
) : ViewModel() {

    private val _selectedDay = MutableStateFlow("") //  "MO", "TU", "WE"
    val selectedDay: StateFlow<String> = _selectedDay

    val medications: StateFlow<List<MedicationSchedule>> = _selectedDay
        .flatMapLatest { day ->
            if (day.isBlank()) flowOf(emptyList())
            else dao.getMedicationsByUserAndDay(userId, day)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setDay(day: String) {
        _selectedDay.value = day  // "MO", "FR", ...
    }

    fun insertMedication(medication: MedicationSchedule) {
        viewModelScope.launch {
            dao.insert(medication.copy(userId = userId)) // vẫn gán userId
        }
    }

    fun updateMedication(medication: MedicationSchedule) {
        viewModelScope.launch {
            dao.update(medication)
        }
    }

    fun deleteMedication(medication: MedicationSchedule) {
        viewModelScope.launch {
            dao.delete(medication)
        }
    }
}

