package com.example.heprotector_.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.heprotector_.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: (Int) -> Unit // Pass user ID back to activity for shared ViewModel
) {
    val username by authViewModel.username.collectAsState()
    val email by authViewModel.email.collectAsState()
    val password by authViewModel.password.collectAsState()
    val height by authViewModel.height.collectAsState()
    val authMessage by authViewModel.authMessage.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // Observe login status to navigate
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            // In a real app, you'd fetch the userId here after successful login from AuthViewModel
            // For now, we assume if isLoggedIn is true, a user ID is implicitly available or handled
            // by sharedViewModel, which AuthViewModel already updates.
            onLoginSuccess(0) // Placeholder, actual user ID would come from login response
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Personal Health Tracker", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = authViewModel::onUsernameChange,
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = authViewModel::onEmailChange,
            label = { Text("Email (for Registration)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = authViewModel::onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = height,
            onValueChange = authViewModel::onHeightChange,
            label = { Text("Height (cm, for Registration)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = authViewModel::signIn) {
                Text("Login")
            }
            Button(onClick = authViewModel::register) {
                Text("Register")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        authMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            LaunchedEffect(it) {
                // Dismiss message after a short delay
                delay(3000)
                authViewModel.dismissAuthMessage()
            }
        }
    }
}