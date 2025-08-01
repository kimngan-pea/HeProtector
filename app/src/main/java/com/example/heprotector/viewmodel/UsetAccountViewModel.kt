package com.example.heprotector.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.data.dao.UserAccountDao
import com.example.heprotector.data.model.UserAccount
import kotlinx.coroutines.launch

class UserAccountViewModel(
    application: Application,
    private val userDao: UserAccountDao
) : AndroidViewModel(application) {

    fun updateIsProfileSetUp(userId: Int, isSetUp: Boolean) {
        println(">> updateIsProfileSetUp called with userId=$userId, isSetUp=$isSetUp")

        viewModelScope.launch {
            val user = userDao.getUserById(userId)
            user?.let {
                val updatedUser = it.copy(isProfileSetUp = isSetUp)
                userDao.update(updatedUser)
            }
        }
    }
}

