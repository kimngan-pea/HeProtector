package com.example.heprotector.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heprotector.data.dao.UserAccountDao

class UserAccountViewModelFactory(
    private val application: Application,
    private val userDao: UserAccountDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserAccountViewModel::class.java)) {
            return UserAccountViewModel(application, userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
