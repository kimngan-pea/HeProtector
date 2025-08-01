package com.example.heprotector.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heprotector.ui.conponents.ActionButtonCard
import androidx.navigation.NavController
import com.example.heprotector.ui.theme.*
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun RecordDataScreen(
    userId: Int?,
    navController: NavController,
    onBackClick: () -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Record Health Data",
                        color = Color.Black,
                        fontSize = 36.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TealA700
                )
            )
        },
        containerColor = greenGray
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            //Buttons
            ActionButtonCard(
                title = "Blood Sugar",
                icon = Icons.Filled.MonitorHeart,
                onClick = { navController.navigate("blood_sugar/$userId") }
            )
            ActionButtonCard(
                title = "HbA1c",
                icon = Icons.Filled.Bloodtype,
                onClick = { navController.navigate("hba1c/$userId") }
            )
            ActionButtonCard(
                title = "Weight",
                icon = Icons.Filled.FitnessCenter,
                onClick = { navController.navigate("weight/$userId") }
            )
        }
    }
}


