package com.example.heprotector.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.data.repository.UserProfileRepository

class UserProfileViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            val dao = AppDatabase.getInstance(application).userProfileDao()
            val repository = UserProfileRepository(dao)
            return UserProfileViewModel(application).also {
            } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
