package com.example.heprotector_.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.heprotector_.ui.theme.HeProtector_Theme
import com.example.heprotector_.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegistrationSuccess: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val username by authViewModel.username.collectAsState()
    val email by authViewModel.email.collectAsState()
    val password by authViewModel.password.collectAsState()
    val confirmPassword by authViewModel.confirmPassword.collectAsState()
    val message by authViewModel.authMessage.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // Observe registration success
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onRegistrationSuccess()
           // authViewModel.clearAuthStatus() // Clear status to prevent re-triggering
        }
    }

    // Observe messages (e.g., registration failure)
//    message?.let {
//        Snackbar(
//            action = {
//                TextButton(onClick = { authViewModel.dismissMessage() }) {
//                    Text("Dismiss")
//                }
//            },
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(it)
//        }
//    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA)) // Light blue background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Back Button
        Align(alignment = Alignment.TopStart) {
            IconButton(onClick = { /* Handle back navigation */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Create New Account",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = authViewModel::onUsernameChange,
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = authViewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = authViewModel::onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = authViewModel::onConfirmPasswordChange,
            label = { Text("Confirm password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { authViewModel.register() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("CREATE", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account?", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(4.dp))
            TextButton(onClick = onSignInClick) {
                Text(
                    text = "Sign in now",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    HeProtector_Theme {
        // You'll need to pass a mock AuthViewModel for preview
        RegisterScreen(
            authViewModel = AuthViewModel(mockk(), mockk()), // Mock dependencies for preview
            onRegistrationSuccess = {},
            onSignInClick = {}
        )
    }
}