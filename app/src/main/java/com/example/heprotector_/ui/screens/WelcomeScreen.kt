package com.example.heprotector_.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.heprotector_.R
import com.example.heprotector_.ui.theme.HeProtector_Theme

@Composable
fun WelcomeScreen(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA)) // Light blue background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo/Image
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your actual logo
            contentDescription = "HeProtector Logo",
            modifier = Modifier.size(180.dp) // Adjusted size for prominence
        )
        Spacer(modifier = Modifier.height(32.dp))

        // App Name
        Text(
            text = "HeProtector",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Welcome Message
        Text(
            text = "Welcome to Health Protector\nSelf-management, convenient, easy, and quick.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))

        // Sign In Button
        Button(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("SIGN IN", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Button
        Button(
            onClick = onSignUpClick,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent), // Outlined button style
            shape = MaterialTheme.shapes.extraSmall, // Use default button shape
            border = ButtonDefaults.outlinedButtonBorder // Use default outlined button border
        ) {
            Text("SIGN UP", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    HeProtector_Theme {
        WelcomeScreen(onSignInClick = {}, onSignUpClick = {})
    }
}
