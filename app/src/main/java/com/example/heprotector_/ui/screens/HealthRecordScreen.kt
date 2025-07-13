package com.example.heprotector_.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.heprotector_.data.db.entities.Health_Record
import com.example.heprotector_.viewmodel.HealthRecordViewModel
import java.time.format.DateTimeFormatter
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthRecordScreen(
    healthRecordViewModel: HealthRecordViewModel,
    onBack: () -> Unit
) {
    val bloodSugar by healthRecordViewModel.bloodSugar.collectAsState()
    val hba1c by healthRecordViewModel.hba1c.collectAsState()
    val weight by healthRecordViewModel.weight.collectAsState()
    val message by healthRecordViewModel.message.collectAsState()
    val healthRecords by healthRecordViewModel.healthRecords.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Data Tracker") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Record New Data", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = bloodSugar,
                onValueChange = healthRecordViewModel::onBloodSugarChange,
                label = { Text("Blood Sugar (mg/dL)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = hba1c,
                onValueChange = healthRecordViewModel::onHba1cChange,
                label = { Text("HbA1c (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = weight,
                onValueChange = healthRecordViewModel::onWeightChange,
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = healthRecordViewModel::recordHealthData,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Record Data")
            }
            Spacer(modifier = Modifier.height(16.dp))

            message?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                LaunchedEffect(it) {
                    delay(3000)
                    healthRecordViewModel.dismissMessage()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Health History (Timeline View)", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            if (healthRecords.isEmpty()) {
                Text("No health records yet. Start by adding some data!", modifier = Modifier.padding(8.dp))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(healthRecords) { record ->
                        HealthRecordCard(record = record)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HealthRecordCard(record: Health_Record) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
            Text(
                text = "Recorded on: ${record.dateRecorded.format(formatter)}",
                style = MaterialTheme.typography.bodyMedium
            )
            record.bloodSugar?.let { Text("Blood Sugar: $it mg/dL", style = MaterialTheme.typography.bodyLarge) }
            record.hba1c?.let { Text("HbA1c: $it %", style = MaterialTheme.typography.bodyLarge) }
            record.weight?.let { Text("Weight: $it kg", style = MaterialTheme.typography.bodyLarge) }
            // You could also calculate and display BMI here if height is available from user
        }
    }
}