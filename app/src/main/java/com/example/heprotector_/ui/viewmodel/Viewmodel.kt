package com.example.heprotector_.ui.viewmodel

import ReminderViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heprotector_.data.db.AppDatabase
import com.example.heprotector_.data.repository.ExerciseSuggestionRepository
import com.example.heprotector_.data.repository.HealthRecordRepository
import com.example.heprotector_.data.repository.MedicationScheduleRepository
import com.example.heprotector_.data.repository.ReminderScheduleRepository
import com.example.heprotector_.data.repository.UserRepository
import com.example.heprotector_.data.repository.LocalFoodRepository
import com.example.heprotector_.viewmodel.AdviceViewModel
import com.example.heprotector_.viewmodel.AuthViewModel
import com.example.heprotector_.viewmodel.HealthRecordViewModel
import com.example.heprotector_.viewmodel.MedicationScheduleViewModel
//import com.example.heprotector_.viewmodel.ReminderViewModel
import com.example.heprotector_.viewmodel.SharedViewModel

// A generic ViewModelFactory to handle different ViewModels
class AppViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getDatabase(application)
        // SharedViewModel should ideally be a singleton if it truly holds app-wide state
        // For simplicity in this factory, we'll instantiate it once and pass it around.
        // In a more complex app, consider Hilt/Koin for proper dependency injection.
        val sharedViewModel = SharedViewModel()

        // Repositories
        val userRepository = UserRepository(database.userDao())
        val healthRecordRepository = HealthRecordRepository(database.healthRecordDao())
        val medicationScheduleRepository = MedicationScheduleRepository(database.medicationScheduleDao())
        val reminderScheduleRepository = ReminderScheduleRepository(database.reminderScheduleDao())
        val exerciseSuggestionRepository = ExerciseSuggestionRepository(database.exerciseSuggestionDao())
        val localFoodRepository = LocalFoodRepository(database.localFoodDao())

        // ViewModel Instantiation
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userRepository, sharedViewModel) as T
        }
        if (modelClass.isAssignableFrom(HealthRecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HealthRecordViewModel(healthRecordRepository, sharedViewModel) as T
        }
        if (modelClass.isAssignableFrom(MedicationScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicationScheduleViewModel(medicationScheduleRepository, sharedViewModel) as T
        }
        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // ReminderViewModel needs context for AlarmManager
            return ReminderViewModel(reminderScheduleRepository, sharedViewModel, application) as T
        }
        if (modelClass.isAssignableFrom(AdviceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdviceViewModel(healthRecordRepository, exerciseSuggestionRepository, localFoodRepository, sharedViewModel) as T
        }
        // Add other ViewModels here as you create them
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}