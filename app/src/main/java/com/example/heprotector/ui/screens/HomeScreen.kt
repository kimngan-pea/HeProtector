package com.example.heprotector.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.example.heprotector.ui.theme.*

@Composable
fun HomeScreen(
    userId: Int,
    onNavigateTo: (String) -> Unit = {},
) {
    Column(
            modifier = Modifier.fillMaxSize()
        ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF094D4D))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "HeProtector",
                color = Color.White,
                fontSize = 60.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            //User information
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFB2EBF2))
                    ) {
                        Text(
                            "Username",
                            color = Color.Black,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF638AAB))
                    ) {
                        Text(
                            "Membership",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Utilities
        Text(
            text = "Utilities",
            modifier = Modifier
                .background(greenGray, shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 30.sp,
            color = Color.White

        )

        Spacer(modifier = Modifier.height(12.dp))

        // Utility cards
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                UtilityCard("Record Data", Icons.Filled.MedicalServices) {
                    onNavigateTo("record/$userId")
                }
                Spacer(modifier = Modifier.width(16.dp))
                UtilityCard("Medication", Icons.Filled.CalendarToday) {
                    onNavigateTo("medication/$userId")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                UtilityCard("Reminder", Icons.Filled.Alarm) {
                    onNavigateTo("reminder/$userId")
                }
                Spacer(modifier = Modifier.width(16.dp))
                UtilityCard("Personalize Advice", Icons.Filled.Info) {
                    onNavigateTo("advice/$userId")
                }
            }
        }
    }
}

@Composable
fun UtilityCard(title: String, icon: ImageVector, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(width = 170.dp, height = 140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC9F8FF)),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = Color(0xFFF5BBE1),
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 20.sp, textAlign = TextAlign.Center)
        }
    }
}


