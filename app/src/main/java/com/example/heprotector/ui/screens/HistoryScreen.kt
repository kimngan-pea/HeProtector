package com.example.heprotector.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heprotector.viewmodel.BloodSugarViewModel
import com.example.heprotector.viewmodel.HbA1cViewModel
import LineChartSection
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.ui.theme.TealA700
import com.example.heprotector.ui.utils.*
import com.example.heprotector.viewmodel.BloodSugarViewModelFactory
import com.example.heprotector.viewmodel.HbA1cViewModelFactory

@Composable
fun HistoryScreen(
    userId: Int?,
) {
    if (userId == null) {
        Text("User ID is missing", color = Color.Red)
        return
    }

    // Initialize ViewModels for BloodSugar and HbA1c
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)

    val bloodSugarViewModel: BloodSugarViewModel = viewModel(
        factory = BloodSugarViewModelFactory(db.bloodSugarDao())
    )
    val hba1cViewModel: HbA1cViewModel = viewModel(
        factory = HbA1cViewModelFactory(db.hba1cDao())
    )

    // Observe data for Blood Sugar and HbA1c
    val bloodSugarList by bloodSugarViewModel.list.observeAsState(emptyList())
    val hba1cList by hba1cViewModel.list.observeAsState(emptyList())

    // Observe user data change (this might trigger fetching of the latest data)
    LaunchedEffect(userId) {
        bloodSugarViewModel.observeDataByUser(userId)
        hba1cViewModel.observeDataByUser(userId)
    }

    // Debugging the lists
    Log.d("DEBUG", "Blood Sugar userid: $userId")

    // For Date Selector (Just for UI)
    var currentDate by remember { mutableStateOf("Jan 20, 2025") }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(TealA700, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "History",
                color = Color.Black,
                fontSize = 36.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Date Selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Previous Date")
            }

            Text(text = currentDate, fontSize = 16.sp)

            IconButton(onClick = {  }) {
                Icon(Icons.Filled.ArrowForward, contentDescription = "Next Date")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Blood sugar chart
        Text("Blood Sugar", fontSize = 16.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            // Blood sugar chart
            if (bloodSugarList.isEmpty()) {
                Text("No Blood Sugar Data Available", color = Color.Gray)
            } else {
                LineChartSection(
                    title = "Blood Sugar Trend",
                    data = bloodSugarList.toChartData() // Ensure the data is properly formatted for the chart
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // HbA1c chart
        Text("HbA1c", fontSize = 16.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            // HbA1c chart
            if (hba1cList.isEmpty()) {
                Text("No HbA1c Data Available", color = Color.Gray)
            } else {
                LineChartSection(
                    title = "HbA1c Trend",
                    data = hba1cList.toChartData() // Ensure the data is properly formatted for the chart
                )
            }
        }
    }
}
