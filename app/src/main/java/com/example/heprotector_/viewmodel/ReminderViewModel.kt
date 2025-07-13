import android.app.Application
import com.example.heprotector_.data.repository.ReminderScheduleRepository
import com.example.heprotector_.viewmodel.SharedViewModel

//package com.example.heprotector_.viewmodel
//
//import android.Manifest
//import android.app.AlarmManager
//import android.app.Application
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.annotation.RequiresPermission
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.heprotector_.db.entities.Reminder_schedule
//import com.example.heprotector_.repository.ReminderScheduleRepository
//import com.example.heprotector_.utils.ReminderBroadcastReceiver
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.filterNotNull
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.launch
//import java.time.LocalTime // For reminderTime
//import java.util.Calendar // <--- Ensure this import is present
//
//class ReminderViewModel(
//    private val reminderScheduleRepository: ReminderScheduleRepository,
//    private val sharedViewModel: SharedViewModel,
//    private val application: Application // Application context for AlarmManager
//) : ViewModel() {
//
//    // ... (existing MutableStateFlows and functions)
//
//    private val _reminderType = MutableStateFlow("Blood Sugar")
//    val reminderType: StateFlow<String> = _reminderType.asStateFlow()
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private val _selectedTime = MutableStateFlow(LocalTime.now().plusMinutes(5))
//    @RequiresApi(Build.VERSION_CODES.O)
//    val selectedTime: StateFlow<LocalTime> = _selectedTime.asStateFlow()
//
//    private val _message = MutableStateFlow<String?>(null)
//    val message: StateFlow<String?> = _message.asStateFlow()
//
//    private val _showTimePickerDialog = MutableStateFlow(false)
//    val showTimePickerDialog: StateFlow<Boolean> = _showTimePickerDialog.asStateFlow()
//
//    val activeReminders: StateFlow<List<Reminder_schedule>> =
//        sharedViewModel.currentUserId
//            .filterNotNull()
//            .combine(reminderScheduleRepository.getActiveRemindersForUser(sharedViewModel.currentUserId.value ?: -1)) { userId, reminders ->
//                reminders.filter { it.userId == userId && it.isActive }
//            }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5000),
//                initialValue = emptyList()
//            )
//
//    fun onReminderTypeChange(type: String) { _reminderType.value = type }
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun onTimeSelected(time: LocalTime) { _selectedTime.value = time }
//    fun showTimePicker() { _showTimePickerDialog.value = true }
//    fun hideTimePicker() { _showTimePickerDialog.value = false }
//    fun dismissMessage() { _message.value = null }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun setReminder() {
//        viewModelScope.launch @androidx.annotation.RequiresPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM) @androidx.annotation.RequiresPermission(
//            android.Manifest.permission.SCHEDULE_EXACT_ALARM
//        ) @androidx.annotation.RequiresPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM) {
//            val userId = sharedViewModel.currentUserId.value
//            if (userId == null) {
//                _message.value = "User not logged in."
//                return@launch
//            }
//
//            val type = _reminderType.value
//            val time = _selectedTime.value
//
//            if (type.isEmpty()) {
//                _message.value = "Please select a reminder type."
//                return@launch
//            }
//
//            try {
//                val newReminder = Reminder_schedule(
//                    userId = userId,
//                    reminderType = type,
//                    reminderTime = time,
//                    isActive = true
//                )
//                val reminderId = reminderScheduleRepository.insertReminderSchedule(newReminder).toInt()
//
//                scheduleAlarm(application.applicationContext, reminderId, time, type)
//
//                _message.value = "Reminder set for $type at $time!"
//                _selectedTime.value = LocalTime.now().plusMinutes(5)
//            } catch (e: Exception) {
//                _message.value = "Error setting reminder: ${e.localizedMessage}"
//            }
//        }
//    }
//
//    private fun Unit.toInt() {
//        TODO("Not yet implemented")
//    }
//
//    private fun ReminderScheduleRepository.insertReminderSchedule(
//        newReminder: Reminder_schedule
//    ) {
//    }
//
//    fun cancelReminder(reminder: Reminder_schedule) {
//        viewModelScope.launch {
//            try {
//                cancelAlarm(application.applicationContext, reminder.reminderId)
//                reminderScheduleRepository.updateReminderSchedule(reminder.copy(isActive = false))
//                _message.value = "Reminder for ${reminder.reminderType} cancelled."
//            } catch (e: Exception) {
//                _message.value = "Error cancelling reminder: ${e.localizedMessage}"
//            }
//        }
//    }
//
//    // --- AlarmManager Helper Functions ---
//
//    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun scheduleAlarm(context: Context, reminderId: Int, reminderTime: LocalTime, reminderType: String) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
//            putExtra("REMINDER_ID", reminderId)
//            putExtra("REMINDER_TYPE", reminderType)
//        }
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            reminderId, // Use reminderId as request code for unique pending intents
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // CORRECTED PART: Initialize Calendar and set its time
//        val calendar = Calendar.getInstance().apply {
//            set(Calendar.HOUR_OF_DAY, reminderTime.hour)
//            set(Calendar.MINUTE, reminderTime.minute)
//            set(Calendar.SECOND, 0)
//            set(Calendar.MILLISECOND, 0)
//
//            // If the scheduled time is in the past (today), set it for tomorrow
//            if (System.currentTimeMillis() > this.timeInMillis) { // <--- 'this' refers to the Calendar instance
//                add(Calendar.DAY_OF_YEAR, 1)
//            }
//        }
//
//        // Check for SCHEDULE_EXACT_ALARM permission on Android 12+ (API 31+)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            if (alarmManager.canScheduleExactAlarms()) {
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    calendar.timeInMillis, // <--- Correct usage
//                    pendingIntent
//                )
//            } else {
//                _message.value = "Please grant 'Alarms & reminders' permission in app settings."
//            }
//        } else // API 23 to 30
//            alarmManager.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis, // <--- Correct usage
//                pendingIntent
//            )
//    }
//
//    private fun cancelAlarm(context: Context, reminderId: Int) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
//            putExtra("REMINDER_ID", reminderId)
//        }
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            reminderId,
//            intent,
//            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
//        )
//        pendingIntent?.let { alarmManager.cancel(it) }
//    }
//}
annotation class ReminderViewModel(
    val reminderScheduleRepository: ReminderScheduleRepository,
    val sharedViewModel: SharedViewModel,
    val application: Application
)
