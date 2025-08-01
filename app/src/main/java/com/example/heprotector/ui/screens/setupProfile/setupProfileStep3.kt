package com.example.heprotector.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heprotector.R
import com.example.heprotector.ui.theme.*
import com.example.heprotector.ui.utils.TempUserProfile

@Composable
fun SetupProfileStep3(
    navController: NavController,
) {
    var selectedType by remember { mutableStateOf(TempUserProfile.diabetesType) }
    var diagnosisMonth by remember { mutableStateOf(TempUserProfile.diagnosisMonth) }
    var diagnosisYear by remember { mutableStateOf(TempUserProfile.diagnosisYear) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // backgroud
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(CyanLight),
            verticalArrangement = Arrangement.SpaceBetween
        )  {
            Column {
                //return
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }

                Text(
                    "Your Type of Diabetes",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueGray
                )

                listOf(
                    "Pre-diabetes",
                    "Type 1 diabetes",
                    "Type 2 diabetes",
                    "Gestational diabetes"
                ).forEach { type ->
                    Button(
                        onClick = {
                            selectedType = type
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedType == type) SkyBlue else Color.White
                        )
                    ) {
                        Text(type, color = Color.Black, fontSize = 20.sp)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    "Year of Diagnosis",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueGray
                )

                Row {
                    OutlinedTextField(
                        value = diagnosisMonth,
                        onValueChange = { diagnosisMonth = it },
                        label = { Text("Month") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = diagnosisYear,
                        onValueChange = { diagnosisYear = it },
                        label = { Text("Year") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(26.dp))

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
                        TempUserProfile.diabetesType = selectedType
                        TempUserProfile.diagnosisMonth = diagnosisMonth
                        TempUserProfile.diagnosisYear = diagnosisYear

                        navController.navigate("setup_profile_4")
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
}
