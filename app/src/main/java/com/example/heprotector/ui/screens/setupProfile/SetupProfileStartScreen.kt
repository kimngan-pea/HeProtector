package com.example.heprotector.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heprotector.ui.theme.*
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.heprotector.R

@Composable
fun SetupProfileStartScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App name
        Text(
            text = "HeProtector",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = greenGray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        // logo
        Image(
            painter = painterResource(id = R.drawable.logo), contentDescription = "Logo image",
            modifier = Modifier.run { size(200.dp) }
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Setup button
        Button(
            onClick = { navController.navigate("setup_profile_1") },
            colors = ButtonDefaults.buttonColors(containerColor = greenLight),
            shape = RectangleShape
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Let's setup profile",
                    color = Color.White,
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Arrow",
                    tint = Color.White
                )
            }
        }

        // Skip button
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.align(Alignment.End),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SkyBlue
            ),
        ) {
            Text("Skip", color = Color.White, fontSize = 24.sp)
        }
    }

}
