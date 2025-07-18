package com.example.heprotector

import android.R.attr.onClick
import android.media.Image
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun SetupProfileScreen() {

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(text = "HeProtector", fontSize = 50.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
        Spacer(modifier = Modifier.height(4.dp))
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo image",
            modifier = Modifier.run { size(250.dp) }
        )

        Text(text = "Welcome to Health Protector", fontSize = 25.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Self-management, convenient, easy, and quick.", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(44.dp))
        Button(onClick = {}) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "Let's setup profile",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Arrow",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(54.dp))
        Button(onClick = {}) {
            Text(text = "Skip", fontSize = 20.sp)
        }
    }

}

