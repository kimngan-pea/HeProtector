package com.example.heprotector.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.ui.text.style.TextDecoration
import com.example.heprotector.ui.theme.*
import com.example.heprotector.viewmodel.AuthViewModel
import com.example.heprotector.viewmodel.AuthViewModelFactory
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.data.pref.UserPreferences
import com.example.heprotector.viewmodel.UserProfileViewModel
import com.example.heprotector.viewmodel.UserProfileViewModelFactory

@Composable
fun SignInScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val userDao = remember { db.userAccountDao() }
    val profileDao = remember { db.userProfileDao() }
    val prefs = remember { UserPreferences(context, profileDao) }

    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(userDao, profileDao, prefs)
    )

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginError by viewModel.loginError

    val userProfileViewModel: UserProfileViewModel = viewModel(
        factory = UserProfileViewModelFactory(context.applicationContext as android.app.Application)
    )

    var userId by remember { mutableStateOf<Int?>(null) }

    val greenGray = Color(0xFF007892)
    val greenLight = Color(0xFF0091AD)

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
            text = "Welcome Back",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = greenGray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username", color = greenLight) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = greenLight) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    viewModel.setRegisterError("Please fill in all fields.")
                } else {
                    viewModel.login(username, password) { id ->
                        userId = id
                        userProfileViewModel.setUserId(id)
                        viewModel.checkUserProfileStatus { isProfileSet ->
                            if (isProfileSet) {
                                println(">> userId = $userId")
                                navController.navigate("home/$id") {
                                    popUpTo("signin") { inclusive = true }
                                }
                            } else {
                                navController.navigate("setup_profile_start") {
                                    popUpTo("signin") { inclusive = true }
                                }
                            }
                        }
                    }

                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = greenGray)
        ) {
            Text(text = "SIGN IN", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))


        Text(
            text = "Don’t have an account?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = BlueGray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        TextButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                " Register now",
                color = Orange,
                fontSize = 18.sp,
                textDecoration = TextDecoration.Underline
            )
        }

        loginError?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}
