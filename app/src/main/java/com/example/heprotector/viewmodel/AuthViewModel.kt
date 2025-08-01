package com.example.heprotector.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heprotector.data.dao.UserAccountDao
import com.example.heprotector.data.dao.UserProfileDao
import com.example.heprotector.data.model.UserAccount
import com.example.heprotector.data.pref.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import com.example.heprotector.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

class AuthViewModel(
    private val userDao: UserAccountDao,
    private val profileDao: UserProfileDao,
    private val prefs: UserPreferences
) : ViewModel() {

    private val _loginError = mutableStateOf<String?>(null)
    val loginError: State<String?> = _loginError


    private val _registerState = mutableStateOf<String?>(null)
    val registerState: State<String?> = _registerState

    fun login(username: String, password: String, onSuccess: (userId: Int) -> Unit) {
        viewModelScope.launch {
            val user = userDao.login(username, password)
            if (user != null) {
                prefs.saveUserId(user.id)
                _loginError.value = null
                onSuccess(user.id)
            } else {
                _loginError.value = "Invalid credentials"
            }
        }
    }
    fun getUserId(): Flow<Int?> {
        return prefs.userIdFlow
    }
    fun signUp(username: String, password: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            val existingUser = userDao.getUserByUsername(username)
            if (existingUser != null) {
                _registerState.value = "Username already exists"
            } else {
                val user = UserAccount(username = username, password = password)
                val userId = userDao.insert(user).toInt()


                val userProfile = createBasicUserProfile(userId, username)
                profileDao.insert(userProfile)
                _registerState.value = "Success"
                onSuccess(userId)
            }
        }
    }
    fun createBasicUserProfile(userId: Int, username: String): UserProfile {
        return UserProfile(
            userId = userId,
            username = username,
            email = username
        )
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            prefs.clearUser()
            onSuccess()
        }
    }


    fun setRegisterError(message: String) {
        _registerState.value = message
    }

    fun resetState() {
        _registerState.value = null
    }

    fun checkUserProfileStatus(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val userId = prefs.userIdFlow.firstOrNull()
            if (userId != null) {
                val user = userDao.getUserById(userId)
                onResult(user?.isProfileSetUp == true)
            } else {
                onResult(false)
            }
        }
    }

}
