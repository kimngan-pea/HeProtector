package com.example.heprotector.ui.screens

import com.example.heprotector.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun WelcomeScreen(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val greenGray = Color(0xFF007892)
    val greenLight = Color(0xFF0091AD)
    val CyanLight = Color(0xFFE0F7FA)

    Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo), contentDescription = "Logo image",
            modifier = Modifier.run { size(200.dp) }
        )
        Spacer(modifier = Modifier.height(6.dp))

        // App Name
        Text(
            text = "HeProtector",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = greenGray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Welcome Message
        Text(
            text = "Welcome to Health Protector\nSelf-management, convenient, \n easy, and quick.",
            style = MaterialTheme.typography.bodyLarge,
            color = greenGray.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Sign In Button
        Button(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = greenGray)
        ) {
            Text("SIGN IN", style = MaterialTheme.typography.titleMedium, color = CyanLight)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Button
        Button(
            onClick = onSignUpClick,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = greenLight)
        ) {
            Text("SIGN UP", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(onSignInClick = {}, onSignUpClick = {})
}