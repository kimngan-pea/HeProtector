package com.example.heprotector

import BottomNavigationBar
import ExercisePlanScreen
import ScheduleReminderScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heprotector.ui.screens.BloodSugarScreen
import com.example.heprotector.ui.screens.DietPlanScreen
import com.example.heprotector.ui.screens.HbA1cScreen
import com.example.heprotector.ui.screens.HistoryScreen
import com.example.heprotector.ui.screens.HomeScreen
import com.example.heprotector.ui.screens.MedicationScheduleScreen
import com.example.heprotector.ui.screens.PersonalizeAdviceScreen
import com.example.heprotector.ui.screens.ProfileScreen
import com.example.heprotector.ui.screens.RecordDataScreen
import com.example.heprotector.ui.screens.RegisterScreen
import com.example.heprotector.ui.screens.SetupProfileStartScreen
import com.example.heprotector.ui.screens.SetupProfileStep1
import com.example.heprotector.ui.screens.SetupProfileStep2
import com.example.heprotector.ui.screens.SetupProfileStep3
import com.example.heprotector.ui.screens.SetupProfileStep4
import com.example.heprotector.ui.screens.SignInScreen
import com.example.heprotector.ui.screens.WeightScreen
import com.example.heprotector.ui.theme.HeProtectorTheme
import com.example.heprotector.ui.screens.WelcomeScreen
import com.example.heprotector.viewmodel.SharedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeProtectorTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun AppBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        content()
    }
}


@Composable
fun MainScreen() {
    AppBackground {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val showBottomBar = currentRoute?.substringBefore("/") in listOf("home", "history", "profile")
        val sharedViewModel: SharedViewModel = viewModel()
        val userId by sharedViewModel.userId.collectAsState()

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController , userId)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "welcome",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("welcome") {
                    WelcomeScreen(
                        onSignInClick = { navController.navigate("signin") },
                        onSignUpClick = { navController.navigate("register") }
                    )
                }
                composable("signin") { SignInScreen(navController) }
                composable("register") { RegisterScreen(navController) }

                composable(
                    route = "home/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    Log.d("DEBUG", "Received userId home: $userId")

                    val userId = backStackEntry.arguments?.getInt("id") ?: return@composable
                    // update into ViewModel
                    LaunchedEffect(Unit) {
                        sharedViewModel.setUserId(userId)
                    }
                    HomeScreen(
                        userId = userId,
                        onNavigateTo = { route ->
                            navController.navigate(route)
                        }
                    )
                }

                composable("setup_profile_start") { SetupProfileStartScreen(navController) }
                composable("setup_profile_1") { SetupProfileStep1(navController) }
                composable("setup_profile_2") { SetupProfileStep2(navController) }
                composable("setup_profile_3") { SetupProfileStep3(navController) }
                composable("setup_profile_4") { SetupProfileStep4(navController) }

                composable(
                    "history/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getInt("id")
                    Log.d("DEBUG", "Received userId history: $userId")

                    if (userId != null) {
                        HistoryScreen(
                            userId = userId
                        )
                    } else {
                        // Log error or handle missing userId
                        Log.e("ERROR", "User ID is missing")
                    }
                }


                composable(
                    route = "profile/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getInt("id") ?: return@composable
                    Log.d("DEBUG", "Received userId profile: $userId")

                    ProfileScreen(
                        userId = userId,
                        navController = navController,
                        onBackClick = { navController.popBackStack() }
                    )
                }



                composable("record/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
                    RecordDataScreen(
                        userId = userId,
                        navController = navController,
                        onBackClick = { navController.popBackStack() }
                    )
                }


                composable("blood_sugar/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
                    BloodSugarScreen(
                        userId = userId,
                        navController = navController,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable("hba1c/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
                    HbA1cScreen(
                        userId = userId,
                        navController = navController,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable("weight/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
                    WeightScreen(
                        userId = userId,
                        navController = navController,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable("medication/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
                    if (userId != null) {
                        MedicationScheduleScreen(
                            userId = userId,
                            onBackClick = { navController.popBackStack() }
                        )
                    } else {

                        navController.popBackStack()
                    }
                }


                composable("reminder/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
                    ScheduleReminderScreen(
                        userId = userId,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable("advice/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
                    PersonalizeAdviceScreen(
                        userId = userId,
                        navController = navController,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable("diet/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
                    DietPlanScreen(
                        userId = userId,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable("exercise/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
                    ExercisePlanScreen(
                        userId = userId,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}