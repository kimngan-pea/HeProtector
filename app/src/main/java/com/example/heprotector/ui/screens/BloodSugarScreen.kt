package com.example.heprotector.ui.screens

import LineChartSection
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.data.model.BloodSugar
import com.example.heprotector.ui.conponents.DropdownSelector
import com.example.heprotector.ui.conponents.HistoryPopup
import com.example.heprotector.ui.conponents.IndicatorLabelRow
import com.example.heprotector.ui.conponents.formatTimestamp
import com.example.heprotector.ui.theme.*
import com.example.heprotector.ui.utils.*
import com.example.heprotector.viewmodel.BloodSugarViewModel
import com.example.heprotector.viewmodel.BloodSugarViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodSugarScreen(
    userId: Int?,
    navController: NavController,
    onBackClick: () -> Unit,

    ) {
    if (userId == null) {
        Text("User ID is missing", color = Color.Red)
        return
    }
    val context = LocalContext.current
    val dao = AppDatabase.getInstance(context).bloodSugarDao()
    val factory = BloodSugarViewModelFactory(dao)
    val viewModel: BloodSugarViewModel = viewModel(factory = factory)

    val list by viewModel.list.observeAsState(emptyList())

    LaunchedEffect(userId) {
        viewModel.observeDataByUser(userId)
    }
    var showHistory by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("Before Meal") }
    var sugarValue by remember { mutableStateOf("") }

    val minValue = list.minByOrNull { it.value }?.value?.toString() ?: "___"
    val maxValue = list.maxByOrNull { it.value }?.value?.toString() ?: "___"
    val avgValue = if (list.isNotEmpty()) String.format("%.2f", list.map { it.value }.average()) else "___"

    val indicators = listOf(
        Triple("Lowest", minValue, Color.Yellow),
        Triple("Average", avgValue, Color.Green),
        Triple("Highest", maxValue, Color.Red)
    )

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(72.dp),
                title = {
                    Text("Blood Sugar", color = Color.Black, fontSize = 30.sp)
                },
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
                        Text(
                            if (showHistory) "Hide History" else "View History",
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = TealA700)
            )
        },
        containerColor = Color.Transparent,
        content = { paddingValues ->
            Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {


                IndicatorLabelRow(data = indicators)

                Spacer(modifier = Modifier.height(16.dp))

                LineChartSection(title = "Blood Sugar Trend", data = list.toChartData())

                // Timepoint selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Timepoint",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = BlueGray,
                        modifier = Modifier.weight(1f)
                    )
                    DropdownSelector(
                        label = "",
                        options = listOf("Before Meal", "After Meal"),
                        value = selectedTime,
                        onValueChange = { selectedTime = it },
                        modifier = Modifier.weight(3f)
                    )
                }

                // Value input
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Blood Value",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = BlueGray,
                        modifier = Modifier.weight(1f)
                    )
                    Box(modifier = Modifier.weight(3f)) {
                        BasicTextField(
                            value = sugarValue,
                            onValueChange = { sugarValue = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        )
                    }
                }
            }

            if (showHistory) {
                HistoryPopup(
                    title = "Blood Sugar History",
                    list = list,
                    onClose = { showHistory = false },
                    itemFormatter = { entry ->
                        val time = formatTimestamp(entry.time)
                        "$time — ${entry.value} (${entry.type})"
                    }
                )
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    val currentTimestamp = System.currentTimeMillis()
                    val value = sugarValue.toFloatOrNull()
                    if (value != null) {
                        val entry = BloodSugar(
                            userId = userId,
                            value = value,
                            time = currentTimestamp,
                            type = selectedTime
                        )
                        viewModel.addEntry(entry)
                        sugarValue = ""
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
    )
}
