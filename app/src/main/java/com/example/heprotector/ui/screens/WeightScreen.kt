package com.example.heprotector.ui.screens

import LineChartSection
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.heprotector.data.model.Weight
import com.example.heprotector.ui.conponents.*
import com.example.heprotector.ui.theme.*
import com.example.heprotector.ui.utils.calculateBMI
import com.example.heprotector.ui.utils.screenHeightDp
import com.example.heprotector.ui.utils.toChartData
import com.example.heprotector.viewmodel.UserProfileViewModel
import com.example.heprotector.viewmodel.WeightViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightScreen(
    userId: Int?,
    navController: NavController,
    onBackClick: () -> Unit
) {
    val userProfileViewModel: UserProfileViewModel = viewModel()
    val userProfile by userProfileViewModel.profile.observeAsState()

    val weightViewModel: WeightViewModel = viewModel()
    val list by weightViewModel.list.observeAsState(emptyList())

// --- UI State ---
    var goalInput by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Current") }
    var weightValue by remember { mutableStateOf("") }
    var showHistory by remember { mutableStateOf(false) }

// --- Extract values from profile ---
    val weight = userProfile?.weight ?: 0f
    val height = userProfile?.height ?: 0f
    val goalWeight = userProfile?.goalWeight

// --- calculate BMI ---
    val bmiFormatted = if (weight > 0f && height > 0f) {
        val bmi = calculateBMI(weight, height)
        String.format("%.2f", bmi)
    } else {
        "___"
    }

// --- Format Goal Weight ---
    val goalWeightText = goalWeight?.let { String.format("%.2f", it) } ?: "___"

// --- display ---
    val indicators = listOf(
        Triple("BMI", bmiFormatted, Color.Red),
        Triple("Current", list.maxByOrNull { it.time }?.value?.let { String.format("%.2f", it) } ?: "___", Color.Yellow),
        Triple("Goal", goalWeightText, Color.Green)
    )

    LaunchedEffect(userId) {
        userId?.let {
            weightViewModel.loadWeights(it)
            userProfileViewModel.setUserId(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weight", color = Color.Black, fontSize = 30.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { showHistory = !showHistory }) {
                        Text(if (showHistory) "Hide History" else "View History", color = Color.Black, fontSize = 18.sp)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = TealA700)
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val value = weightValue.toFloatOrNull()
                    if (value != null) {
                        userId?.let { id ->
                            weightViewModel.addEntry(
                                Weight(
                                    value = value,
                                    userId = id,
                                    time = System.currentTimeMillis()
                                )
                            )
                        }



                        userProfile?.let {
                            val updatedProfile = it.copy(weight = value)
                            userProfileViewModel.saveProfile(updatedProfile)
                            println(">> Profile updated with new weight: $value")
                        }

                        weightValue = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenGray)
            ) {
                Text("Save", color = Color.White, fontSize = 20.sp)
            }
        }
    ) { paddingValues ->
        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .heightIn(max = screenHeightDp())
                    .verticalScroll(rememberScrollState())
            ) {
            IndicatorLabelRow(data = indicators)
            Spacer(modifier = Modifier.height(6.dp))

            LineChartSection(
                title = "Weight Chart",
                data = list.toChartData(),
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Input current weight
            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weight",
                    fontWeight = FontWeight.Bold,
                    color = BlueGray,
                    modifier = Modifier.width(80.dp),
                    fontSize = 20.sp

                )

                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = weightValue,
                    onValueChange = { weightValue = it },
                    label = { Text("e.g., 60") },
                    modifier = Modifier.width(180.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Kg", fontWeight = FontWeight.Bold)
            }


            Spacer(modifier = Modifier.height(4.dp))

            // Update goal weight
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Goal",
                    fontWeight = FontWeight.Bold,
                    color = BlueGray,
                    modifier = Modifier.width(80.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = goalInput,
                    onValueChange = { goalInput = it },
                    label = { Text("e.g., 55") },
                    modifier = Modifier
                        .width(180.dp)
                        .heightIn(min = 70.dp), // Chiều cao chuẩn cho OutlinedTextField
                    singleLine = true,
                )

                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        println("UserProfileViewModel: ${userProfileViewModel.hashCode()}")

                        val goal = goalInput.toFloatOrNull()
                        println("Goal input: $goalInput, parsed: $goal")

                        val profile = userProfile
                        println("User profile: $profile")
                        if (goal != null && profile != null) {
                            val updatedProfile = profile.copy(goalWeight = goal)
                            userProfileViewModel.saveProfile(updatedProfile)
                            goalInput = ""
                        }
                    },
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Update")
                }
            }
        }

        // History popup
        if (showHistory) {
            HistoryPopup(
                title = "Weight History",
                list = list,
                onClose = { showHistory = false },
                itemFormatter = { entry ->
                    val time = formatTimestamp(entry.time)
                    "$time — ${entry.value} Kg"
                }
            )
        }
    }
}
