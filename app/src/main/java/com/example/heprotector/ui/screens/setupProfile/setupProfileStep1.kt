package com.example.heprotector.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Icon
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heprotector.ui.theme.*
import com.example.heprotector.ui.utils.TempUserProfile
import com.example.heprotector.ui.utils.parseDateToMillis

@Composable
fun SetupProfileStep1(
    navController: NavController,
) {
    var selectedTarget by remember { mutableStateOf("") }
    var dobText by remember { mutableStateOf("") }
    var dobMillis by remember { mutableStateOf(0L) }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(CyanLight),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
        Column {
            IconButton(onClick = { navController.popBackStack() }) {
                androidx.compose.material3.Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }

            Text(
                "Your Management",
                fontSize = 26.sp,
                color = BlueGray,
                fontWeight = FontWeight.Bold,
            )

            listOf("Yourself", "Your parents", "Your grandparents").forEach { label ->
                Button(
                    onClick = { selectedTarget = label },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTarget == label) SkyBlue else Color.White
                    )
                ) {
                    Text(label, color = Color.Black, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "Your Date of Birth",
                color = BlueGray,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )

            OutlinedTextField(
                value = dobText,
                onValueChange = {
                    dobText = it
                    dobMillis = parseDateToMillis(it)
                },

                label = { Text("Select date") },
                modifier = Modifier.fillMaxWidth()
            )
        }



        Button(
            onClick = {
                // temporary data
                TempUserProfile.management = selectedTarget
                TempUserProfile.dateOfBirth = dobMillis

                navController.navigate("setup_profile_2")
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = greenGray)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Next", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Arrow",
                    tint = Color.White
                )
            }
        }
    }
}
