package com.example.heprotector_

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.heprotector_.ui.screens.HealthRecordScreen
import com.example.heprotector_.ui.screens.RegisterScreen
import com.example.heprotector_.ui.screens.SignInScreen
import com.example.heprotector_.ui.screens.WelcomeScreen
import com.example.heprotector_.viewmodel.AuthViewModel
import com.example.heprotector_.viewmodel.SharedViewModel

object Routes {
    const val WELCOME = "welcome"
    const val SIGN_IN = "signIn"
    const val REGISTER = "register"
    const val HOME = "home" // This will be the main content after login
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeProtectorApp(
    authViewModel: AuthViewModel,
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val currentUserId by sharedViewModel.currentUserId.collectAsState()

    // Determine the start destination based on whether a user is logged in
    val startDestination = if (currentUserId != null) {
        Routes.HOME
    } else {
        Routes.WELCOME
    }

    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {

        composable(Routes.WELCOME) {
            WelcomeScreen(
                onSignInClick = { navController.navigate(Routes.SIGN_IN) },
                onSignUpClick = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.SIGN_IN) {
            SignInScreen(
                authViewModel = authViewModel,
                onSignInSuccess = { navController.navigate(Routes.HOME) {
                    popUpTo(Routes.WELCOME) { inclusive = true } // Clear back stack to prevent going back to auth screens
                }},
                onRegisterNowClick = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegistrationSuccess = { navController.navigate(Routes.HOME) {
                    popUpTo(Routes.WELCOME) { inclusive = true } // Clear back stack
                }},
                onSignInClick = { navController.navigate(Routes.SIGN_IN) }
            )
        }

        composable(Routes.HOME) {
            // This is where your main app content will go after successful login/registration.
            // For now, let's just put HealthRecordScreen as an example.
            // You'll eventually replace this with a Scaffold and BottomNavigation/Drawer
            // leading to all main features.
            // Placeholder for the main app content
        }

        // Add other composable destinations here as you build them
        // composable(Routes.MEDICATIONS) { MedicationScheduleScreen() }
        // composable(Routes.REMINDERS) { ReminderScreen() }
        // composable(Routes.ADVICE) { AdviceScreen() }
    }
}