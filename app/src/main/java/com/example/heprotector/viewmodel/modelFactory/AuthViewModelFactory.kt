package com.example.heprotector.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heprotector.data.dao.UserAccountDao
import com.example.heprotector.data.dao.UserProfileDao
import com.example.heprotector.data.pref.UserPreferences

class AuthViewModelFactory(
    private val userDao: UserAccountDao,
    private val profileDao: UserProfileDao,
    private val prefs: UserPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userDao, profileDao, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
