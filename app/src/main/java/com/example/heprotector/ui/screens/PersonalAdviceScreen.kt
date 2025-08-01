package com.example.heprotector.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.*
import com.example.heprotector.ui.theme.*
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.heprotector.ui.conponents.ActionButtonCard


@Composable
fun PersonalizeAdviceScreen(
    navController: NavController,
    onBackClick : () -> Unit = {},
    userId: Int?
) {
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onBackClick() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Personalize Advice",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        ActionButtonCard(
            title = "Diet",
            description = "Plan for your meal",
            icon = Icons.Default.Fastfood,
            iconTint = Pink80,
            backgroundColor = Color.White,
            onClick = { navController.navigate("diet/$userId") }
        )
        Spacer(modifier = Modifier.height(40.dp))
        //fix card backgroundColor
        ActionButtonCard(
            title = "Exercise",
            description = "Plan for your activity",
            icon = Icons.Default.FitnessCenter,
            iconTint = Pink80,
            backgroundColor = Color.White,
            onClick = { navController.navigate("exercise/$userId") }
        )
    }
}
