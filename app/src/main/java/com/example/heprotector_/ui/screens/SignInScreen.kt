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
import com.example.heprotector_.data.repository.UserRepository
import com.example.heprotector_.ui.theme.HeProtector_Theme
import com.example.heprotector_.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    authViewModel: AuthViewModel,
    onSignInSuccess: () -> Unit,
    onRegisterNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val username by authViewModel.username.collectAsState()
    val password by authViewModel.password.collectAsState()
    val message by authViewModel.authMessage.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // Observe login success
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onSignInSuccess()
            //authViewModel.clearAuthStatus() // Clear status to prevent re-triggering on recompose
        }
    }

    // Observe messages (e.g., login failure)
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
            IconButton(onClick = { /* Handle back navigation, possibly navController.popBackStack() */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = authViewModel::onUsernameChange,
            label = { Text("Email / Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email) // Or Text
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
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { authViewModel.signIn() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("SIGN IN", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an account?", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(4.dp))
            TextButton(onClick = onRegisterNowClick) {
                Text(
                    text = "Register now?",
                    color = MaterialTheme.colorScheme.primary, // Or a distinct link color
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


// Helper composable for alignment (if needed for custom top-start placement)
@Composable
fun Align(
    alignment: Alignment,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.align(alignment)) {
            content()
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    HeProtector_Theme {
        // You'll need to pass a mock AuthViewModel for preview
        SignInScreen(
            authViewModel = AuthViewModel(mockk(), mockk()), // Mock dependencies for preview
            onSignInSuccess = {},
            onRegisterNowClick = {}
        )
    }
}

fun mockk(): UserRepository {
    return TODO("Provide the return value")
}

