package com.example.heprotector_.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heprotector_.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AuthViewModel(private val userRepository: UserRepository, private val sharedViewModel: SharedViewModel) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _height = MutableStateFlow("") // As string for input
    val height: StateFlow<String> = _height.asStateFlow()

    private val _authMessage = MutableStateFlow<String?>(null)
    val authMessage: StateFlow<String?> = _authMessage.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun onUsernameChange(newUsername: String) { _username.value = newUsername }
    fun onEmailChange(newEmail: String) { _email.value = newEmail }
    fun onPasswordChange(newPassword: String) { _password.value = newPassword }
    fun onConfirmPasswordChange(newConfirmPassword: String) { _confirmPassword.value = newConfirmPassword}
    fun onHeightChange(newHeight: String) { _height.value = newHeight }
    fun dismissAuthMessage() { _authMessage.value = null }


    fun register() {
        viewModelScope.launch {
            try {
                // In a real app, hash the password securely
                val hashedPassword = password.value // Placeholder
                val hashedConfirmPassword = confirmPassword.value
                val parsedHeight = height.value.toDoubleOrNull()

                val newUserId = userRepository.registerUser(
                    username.value,
                    email.value,
                    hashedPassword,
                    parsedHeight
                )
                if (newUserId > 0) {
                    _authMessage.value = "Registration successful!"
                    _isLoggedIn.value = true
                    sharedViewModel.setLoggedInUser(newUserId.toInt())
                } else {
                    _authMessage.value = "Registration failed."
                }
            } catch (e: Exception) {
                _authMessage.value = "Error during registration: ${e.message}"
            }
        }
    }

    private fun UserRepository.setLoggedInUser(toInt: Int) {}

    fun signIn() {
        viewModelScope.launch {
            try {
                val hashedPassword = password.value // Placeholder
                val user = userRepository.loginUser(username.value, hashedPassword)
                if (user != null) {
                    _authMessage.value = "Login successful!"
                    _isLoggedIn.value = true
                    sharedViewModel.setLoggedInUser(user.userId)
                } else {
                    _authMessage.value = "Invalid username or password."
                }
            } catch (e: Exception) {
                _authMessage.value = "Error during login: ${e.message}"
            }
        }
    }

    private fun UserRepository.logoutUser(toInt: Int) {}
    
    fun logout() {
        _isLoggedIn.value = false
        sharedViewModel.logoutUser(
            toInt = TODO()
        )
        _authMessage.value = "Logged out."
    }
}