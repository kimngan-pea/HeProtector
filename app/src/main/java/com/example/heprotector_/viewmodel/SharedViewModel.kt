package com.example.heprotector_.viewmodel

//To manage current user ID, etc.

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {
    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId: StateFlow<Int?> = _currentUserId.asStateFlow()

    fun setLoggedInUser(userId: Int) {
        _currentUserId.value = userId
    }

    fun logoutUser() {
        _currentUserId.value = null
    }

    // You might also add a StateFlow for showing SnackBar messages, etc.
}