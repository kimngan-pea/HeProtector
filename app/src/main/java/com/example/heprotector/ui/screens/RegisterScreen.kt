package com.example.heprotector.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.data.pref.UserPreferences
import com.example.heprotector.ui.theme.*
import com.example.heprotector.viewmodel.AuthViewModel
import com.example.heprotector.viewmodel.AuthViewModelFactory

@Composable
fun RegisterScreen(
    navController: NavController,
) {
    // take context and create dependency
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val prefs = remember { UserPreferences(context, db.userProfileDao()) }

    // create AuthViewModel by Factory
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            userDao = db.userAccountDao(),
            profileDao = db.userProfileDao(),
            prefs = prefs
        )
    )

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val registerState by authViewModel.registerState

    LaunchedEffect(registerState) {
        if (registerState == "Success") {
            authViewModel.resetState()
            navController.navigate("signin")
        }
    }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = BlueGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Create New Account",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = greenGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username", color = greenLight) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = greenLight) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = greenLight) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm password", color = greenLight) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    authViewModel.setRegisterError("Please fill in all fields.")
                } else if (password != confirmPassword) {
                    authViewModel.setRegisterError("Passwords do not match.")
                } else {
                    authViewModel.signUp(username, password) {
                        // onSuccess callback
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = greenGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CREATE", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Already have an account?",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BlueGray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        TextButton(onClick = { navController.navigate("signin") }) {
            Text(
                "Sign in now",
                color = Purple40,
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline
            )

        }

        registerState?.let {
            if (it != "Success") {
                Spacer(modifier = Modifier.height(14.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
