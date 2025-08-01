package com.example.heprotector.ui.screens

import LineChartSection
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.ui.conponents.HistoryPopup
import com.example.heprotector.ui.conponents.IndicatorLabelRow
import com.example.heprotector.ui.conponents.formatTimestamp
import com.example.heprotector.ui.theme.*
import com.example.heprotector.ui.utils.*
import com.example.heprotector.viewmodel.HbA1cViewModel
import com.example.heprotector.viewmodel.HbA1cViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HbA1cScreen(
    userId: Int?,
    navController: NavController,
    onBackClick: () -> Unit
) {
    if (userId == null) {
        Text("User ID not found")
        return
    }

    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val viewModel: HbA1cViewModel = viewModel(
        factory = HbA1cViewModelFactory(db.hba1cDao())
    )

    val list by viewModel.list.observeAsState(emptyList())

    LaunchedEffect(userId) {
        viewModel.observeDataByUser(userId)
    }

    var selectedOption by remember { mutableStateOf("Latest") }
    var hba1cValue by remember { mutableStateOf("") }
    var showHistory by remember { mutableStateOf(false) }

    val indicators = listOf(
        Triple(
            "Lowest",
            list.minByOrNull { it.value }?.value?.let { String.format("%.2f", it) } ?: "___",
            Color.Yellow
        ),
        Triple(
            "Average",
            list.takeIf { it.isNotEmpty() }?.map { it.value }?.average()?.let { String.format("%.2f", it) } ?: "___",
            Color.Green
        ),
        Triple(
            "Highest",
            list.maxByOrNull { it.value }?.value?.let { String.format("%.2f", it) } ?: "___",
            Color.Red
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HbA1c", color = Color.Black, fontSize = 30.sp) },
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
                    val value = hba1cValue.toFloatOrNull()
                    if (value != null) {
                        viewModel.insertHbA1c(value, userId)
                        hba1cValue = ""
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
        ) {

            IndicatorLabelRow(data = indicators)

            Spacer(modifier = Modifier.height(18.dp))

            // chart
            LineChartSection(
                title = "HbA1c Chart",
                data = list.toChartData(),
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // get value
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Enter HbA1c",
                    fontWeight = FontWeight.Bold,
                    color = BlueGray,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = hba1cValue,
                    onValueChange = { hba1cValue = it },
                    label = { Text("e.g., 5.6") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("%", fontWeight = FontWeight.Bold)
            }
        }

        if (showHistory) {
            if (list.isEmpty()) {
                EmptyHistoryDialog(onClose = { showHistory = false })
            } else {
                HistoryPopup(
                    title = "HbA1c History",
                    list = list,
                    onClose = { showHistory = false },
                    itemFormatter = { entry ->
                        val time = formatTimestamp(entry.time)
                        "$time — ${entry.value} %"
                    }
                )
            }
        }
    }
}
@Composable
fun EmptyHistoryDialog(onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(onClick = onClose) {
                Text("Đóng")
            }
        },
        title = { Text("Lịch sử HbA1c") },
        text = { Text("Chưa có dữ liệu lịch sử.") }
    )
}


