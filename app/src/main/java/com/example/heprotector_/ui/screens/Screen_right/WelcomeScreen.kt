package com.example.heprotector

import android.media.Image
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen() {

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(text = "HeProtector", fontSize = 50.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
        Spacer(modifier = Modifier.height(4.dp))
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo image",
            modifier = Modifier.run { size(200.dp) }
        )

        Text(text = "Welcome to Health Protector", fontSize = 25.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Self-management, convenient, easy, and quick.", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(54.dp))
        Button(onClick = {}) {
            Text(text = "SIGN IN", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {}) {
            Text(text = "REGISTER", fontSize = 20.sp)
        }
    }

}