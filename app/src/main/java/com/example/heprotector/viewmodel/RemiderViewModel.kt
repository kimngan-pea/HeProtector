package com.example.heprotector.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heprotector.data.dao.ReminderDao
import com.example.heprotector.data.model.ReminderItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
class ReminderViewModel(
    private val dao: ReminderDao,
    private val userId: Int
) : ViewModel() {

    private val _selectedDay = MutableStateFlow("MO") // hoặc bạn có thể set mặc định khác
    private val _selectedType = MutableStateFlow("Blood Sugar") // hoặc mặc định khác

    fun getReminders(): StateFlow<List<ReminderItem>> {
        return dao.getReminders(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
    // Thêm hoặc cập nhật Reminder
    fun saveReminder(reminder: ReminderItem) {
        viewModelScope.launch {
            dao.insertReminder(reminder)
        }
    }

    // Cập nhật Reminder
    fun updateReminder(reminder: ReminderItem) {
        viewModelScope.launch {
            dao.updateReminder(reminder)
        }
    }


    // Xoá Reminder
    fun deleteReminder(reminder: ReminderItem) {
        viewModelScope.launch {
            dao.deleteReminder(reminder)
        }
    }
}