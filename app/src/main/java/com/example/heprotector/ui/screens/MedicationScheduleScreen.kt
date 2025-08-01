package com.example.heprotector.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.data.model.MedicationSchedule
import com.example.heprotector.ui.utils.*
import com.example.heprotector.viewmodel.MedicationViewModel
import com.example.heprotector.viewmodel.modelFactory.MedicationViewModelFactory
import DayTabRow
import com.example.heprotector.ui.theme.PurpleGrey80
import com.example.heprotector.ui.theme.TealA700
import com.example.heprotector.ui.theme.greenGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationScheduleScreen(
    userId: Int?,
    onBackClick: () -> Unit = {},
) {
    if (userId == null) {
        Text("Invalid user")
        return
    }

    val context = LocalContext.current
    val dao = AppDatabase.getInstance(context).medicationDao()
    val factory = remember(userId) { MedicationViewModelFactory(dao, userId) }
    val viewModel: MedicationViewModel = viewModel(factory = factory)

    val BlueGray = Color(0xFF3674B5)
    val BlueExtraLight = Color(0xFFE3F2FD)

    var selectedDay by remember { mutableStateOf(getTodayShortDay()) }
    var medications by remember { mutableStateOf(mutableListOf<MedicationSchedule>()) }

    val scheduledMeds by viewModel.medications.collectAsState()

    // update ViewModel
    LaunchedEffect(selectedDay) {
        viewModel.setDay(selectedDay)
    }

    LaunchedEffect(scheduledMeds) {
        medications = scheduledMeds.toMutableList()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Medication Schedule", color = Color.Black, fontSize = 36.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = TealA700)
            )
        },
        containerColor = Color.Transparent,
        bottomBar = {
            Button(
                onClick = {
                    viewModel.setDay(selectedDay)
                    scheduledMeds.forEach { viewModel.deleteMedication(it) }

                    medications.filter {
                        it.medicine.isNotBlank() &&
                                it.hour.toString().toIntOrNull() != null &&
                                it.minute.toString().toIntOrNull() != null
                    }.forEach {
                        val newMed = it.copy(
                            id = 0,
                            day = selectedDay,  // Day: "MO", "FR"...
                            userId = userId
                        )
                        println("Insert new medicine: $newMed")
                        viewModel.insertMedication(newMed)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenGray)
            ) {
                Text("Save", color = Color.White, fontSize = 20.sp)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            DayTabRow(
                selectedDay = selectedDay,
                onDaySelected = { selectedDay = it }
            )

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Medication List",
                    style = MaterialTheme.typography.titleMedium,
                    color = BlueGray,
                    fontSize = 24.sp
                )
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        val newId = (medications.maxOfOrNull { it.id } ?: 0) + 1
                        medications = medications.toMutableList().apply {
                            add(
                                MedicationSchedule(
                                    id = newId,
                                    medicine = "",
                                    dosage = "",
                                    hour = 0,
                                    minute = 0,
                                    day = selectedDay, // Day
                                    userId = userId
                                )
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleGrey80)
                ) {
                    Text("Add", color = Color.White)
                }
            }

            Spacer(Modifier.height(8.dp))

            MedicationTable(
                medications = medications,
                onUpdate = { updatedItem ->
                    medications = medications.map {
                        if (it.id == updatedItem.id) updatedItem else it
                    }.toMutableList()
                },
                onDelete = { itemToRemove ->
                    medications = medications.filter { it.id != itemToRemove.id }
                        .mapIndexed { index, item -> item.copy(id = index + 1) }
                        .toMutableList()
                }
            )
        }
    }
}

@Composable
fun MedicationTable(
    medications: List<MedicationSchedule>,
    onUpdate: (MedicationSchedule) -> Unit,
    onDelete: (MedicationSchedule) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
            .background(Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0))
                .padding(8.dp)
        ) {
            TableHeaderCell("Medicine", 3f)
            TableHeaderCell("Dosage", 2f)
            TableHeaderCell("Time", 3f)
            TableHeaderCell("Del", 1f)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(medications, key = { it.id }) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = item.medicine,
                        onValueChange = { onUpdate(item.copy(medicine = it)) },
                        placeholder = { Text("Name") },
                        modifier = Modifier.weight(3f),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = item.dosage,
                        onValueChange = { onUpdate(item.copy(dosage = it.filter(Char::isDigit))) },
                        placeholder = { Text("mg") },
                        modifier = Modifier
                            .weight(2f)
                            .padding(horizontal = 4.dp),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.weight(3f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = item.hour.toString().padStart(2, '0'),
                            onValueChange = {
                                val intVal = it.toIntOrNull()
                                if (intVal != null && intVal in 0..23) {
                                    onUpdate(item.copy(hour = intVal))
                                }
                            },
                            placeholder = { Text("HH") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Text(":", Modifier.padding(horizontal = 4.dp))
                        OutlinedTextField(
                            value = item.minute.toString().padStart(2, '0'),
                            onValueChange = {
                                val intVal = it.toIntOrNull()
                                if (intVal != null && intVal in 0..59) {
                                    onUpdate(item.copy(minute = intVal))
                                }
                            },
                            placeholder = { Text("MM") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    IconButton(onClick = { onDelete(item) }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableHeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(4.dp),
        fontSize = 20.sp,
        color = Color.Black
    )
}
