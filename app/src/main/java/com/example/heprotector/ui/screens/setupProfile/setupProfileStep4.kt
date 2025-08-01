package com.example.heprotector.ui.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.data.model.Weight
import com.example.heprotector.data.pref.UserPreferences
import com.example.heprotector.ui.theme.*
import com.example.heprotector.ui.utils.TempUserProfile
import com.example.heprotector.viewmodel.UserAccountViewModel
import com.example.heprotector.viewmodel.UserAccountViewModelFactory
import com.example.heprotector.viewmodel.UserProfileViewModel
import com.example.heprotector.viewmodel.UserProfileViewModelFactory
import com.example.heprotector.viewmodel.WeightViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SetupProfileStep4(
    navController: NavController,
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val profileDao = remember { db.userProfileDao() }
    val prefs = remember { UserPreferences(context, profileDao) }

    val userProfileViewModel: UserProfileViewModel = viewModel(
        factory = UserProfileViewModelFactory(context.applicationContext as android.app.Application)
    )
    val weightViewModel: WeightViewModel = viewModel()
    var userId by remember { mutableStateOf<Int?>(null) }
    val profile by userProfileViewModel.profile.observeAsState()

    // take userId from UserPreferences
    LaunchedEffect(Unit) {
        prefs.userIdFlow.collectLatest { id ->
            if (id != null) {
                userId = id
                userProfileViewModel.setUserId(id)
            }
        }
    }
    LaunchedEffect(profile) {
        profile?.let { acc ->
            TempUserProfile.username = acc.username ?: ""
            TempUserProfile.fullName = acc.email ?: ""
        }
    }

    val userDao = remember { db.userAccountDao() }

    val userAccountViewModel: UserAccountViewModel = viewModel(
        factory = UserAccountViewModelFactory(
            context.applicationContext as Application,
            userDao
        )
    )

    var height by remember { mutableStateOf(TempUserProfile.height) }
    var weight by remember { mutableStateOf(TempUserProfile.weight) }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(CyanLight),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
        Column {
            // Return
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }


            // Height
            Text(
                "Your Height",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = BlueGray
            )
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Enter your height", fontSize = 20.sp) },
                trailingIcon = { Text("cm", color = BlueGray) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))


            // Weight
            Text(
                "Your Weight",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = BlueGray
            )
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Enter your weight") },
                trailingIcon = { Text("kg", color = BlueGray, fontSize = 20.sp) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Next or Previous
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Purple80)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Previous", color = Black, fontSize = 20.sp)

                }
            }
            Button(
                onClick = {
                    TempUserProfile.height = height
                    TempUserProfile.weight = weight

                    userId?.let { id ->
                        val profile = TempUserProfile.toUserProfile(id)

                        val currentWeight = weight.toFloatOrNull()
                        val weightEntry = currentWeight?.let {
                            Weight(
                                userId = id,
                                value = it,
                                time = System.currentTimeMillis()
                            )
                        }
                        // Launch coroutine
                        CoroutineScope(Dispatchers.Main).launch {
                            userProfileViewModel.saveProfile(profile)
                            userAccountViewModel.updateIsProfileSetUp(id, true)

                            if (weightEntry != null) {
                                weightViewModel.addEntry(weightEntry)
                            }

                            delay(100)

                            navController.navigate("home/$id") {
                                popUpTo("setup_profile_1") { inclusive = true }
                            }
                        }
                    }

                },
                colors = ButtonDefaults.buttonColors(containerColor = Orange)
            ) {
                Text("Finish", color = Color.White, fontSize = 20.sp)
            }
        }
    }
}
