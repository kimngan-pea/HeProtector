package com.example.heprotector_.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.heprotector_.viewmodel.AuthViewModel
import com.example.heprotector_.viewmodel.SharedViewModel

@Composable
fun MainDashboardScreen(
    onNavigateToHealthRecord: () -> Unit,
    onNavigateToMedication: () -> Unit,
    onNavigateToReminder: () -> Unit,
    onNavigateToAdvice: () -> Unit,
    onLogout: () -> Unit,
    sharedViewModel: SharedViewModel,
    authViewModel: AuthViewModel
) {
    val currentUserId by sharedViewModel.currentUserId.collectAsState()
    val username by authViewModel.username.collectAsState() // Assumes AuthViewModel keeps username

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome, ${username.ifEmpty { "User" }}!", style = MaterialTheme.typography.headlineMedium)
        if (currentUserId != null) {
            Text("User ID: $currentUserId", style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateToHealthRecord,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Track Health Data")
        }
        Button(
            onClick = onNavigateToMedication,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Medication Schedules")
        }
        Button(
            onClick = onNavigateToReminder,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Set Reminders")
        }
        Button(
            onClick = onNavigateToAdvice,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Food & Exercise Advice")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Logout")
        }
    }
}