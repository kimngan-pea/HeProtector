package com.example.heprotector.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heprotector.ui.theme.*
import com.example.heprotector.ui.utils.TempUserProfile

@Composable
fun SetupProfileStep2(
    navController: NavController,
) {
    var selectedTarget by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Ho Chi Minh City") }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(CyanLight),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
        Column {
            //return
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }


            // Gender
            Text(
                "Your Gender",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = BlueGray
            )

            Column {
                listOf("Female", "Male")
                    .forEach { label ->
                        Button(
                            onClick = {
                                selectedTarget = label
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTarget == label) SkyBlue else Color.White
                            )
                        ) {
                            Text(label, color = Color.Black, fontSize = 20.sp)
                        }
                    }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Location
            Text(
                "Your Location",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = BlueGray
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                modifier = Modifier.fillMaxWidth(),
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
                    TempUserProfile.gender = selectedGender
                    TempUserProfile.location = location


                    navController.navigate("setup_profile_3")
                },
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
}
