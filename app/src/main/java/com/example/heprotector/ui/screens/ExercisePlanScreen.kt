import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.data.model.ExerciseItem
import com.example.heprotector.viewmodel.ExerciseViewModel
import com.example.heprotector.viewmodel.ExerciseViewModelFactory
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.graphics.Color
import com.example.heprotector.ui.theme.TealA700
import com.example.heprotector.ui.theme.greenGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisePlanScreen(
    userId: Int?,
    onBackClick: () -> Unit
) {
    var currentDay by remember { mutableStateOf("MO") }
    val context = LocalContext.current

    // ViewModel setup
    val exerciseDao = AppDatabase.getInstance(context).exerciseDao()
    val factory = remember(userId) { ExerciseViewModelFactory(exerciseDao, userId ?: 0) }
    val exerciseViewModel: ExerciseViewModel = viewModel(factory = factory)
    var showAddDialog by remember { mutableStateOf(false) }

    // Load exercises from Room when day changes
    LaunchedEffect(currentDay) {
        exerciseViewModel.getExercisesForDay(currentDay) // This ensures data is loaded when day changes
    }

    // Observe exercises from ViewModel
    val exercises by exerciseViewModel.getExercisesForDay(currentDay).collectAsState(initial = emptyList())

    // Use remember to store and maintain exercises in state
    var exercisesList by remember(currentDay, exercises) {
        mutableStateOf(exercises.mapIndexed { index, exercise ->
            ExerciseItem(
                id = index + 1,
                name = exercise.name,
                duration = exercise.duration,
                unit = exercise.unit,
                day = currentDay,
                userId = userId ?: 0
            )
        }.toMutableList())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exercise Plan", fontSize = 36.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Exercise")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = TealA700)
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    // Save exercises for the current day
                    exerciseViewModel.saveExercises(currentDay, exercisesList)
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
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DayTabRow(
                selectedDay = currentDay,
                onDaySelected = { selected -> currentDay = selected }
            )

            val totalDuration = exercisesList.sumOf { it.duration }
            Text(
                "Total: $totalDuration min",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            PlanList(
                items = exercisesList,
                onRemoveItem = { item ->
                    if (item is ExerciseItem) {
                        // Delete exercise
                        exerciseViewModel.deleteExercise(item)
                    }
                },
                modifier = Modifier.weight(1f)
            )

            if (showAddDialog) {
                val exerciseSuggestions = listOf(
                    Triple("Running", 30, "min"),
                    Triple("Cycling", 45, "min"),
                    Triple("Swimming", 20, "min"),
                    Triple("Jumping Rope", 15, "min"),
                    Triple("Yoga", 60, "min"),
                    Triple("Other", 0, "min") // Option for custom input
                )

                var selectedSuggestion by remember {
                    mutableStateOf<Triple<String, Int, String>?>(
                        null
                    )
                }
                var name by remember { mutableStateOf("") }
                var duration by remember { mutableStateOf("") }
                var unit by remember { mutableStateOf("min") }

                var suggestionExpanded by remember { mutableStateOf(false) }

                // Update name, duration, and unit based on selected suggestion
                LaunchedEffect(selectedSuggestion) {
                    selectedSuggestion?.let {
                        if (it.first != "Other") {
                            name = it.first
                            duration = it.second.toString()
                            unit = it.third
                        } else {
                            name = ""
                            duration = ""
                            unit = "min"
                        }
                    }
                }

                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    title = { Text("Add New Exercise") },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Dropdown to choose exercise from suggestions
                            Box {
                                OutlinedTextField(
                                    value = selectedSuggestion?.first ?: "",
                                    onValueChange = {},
                                    label = { Text("Choose from suggestions") },
                                    modifier = Modifier.fillMaxWidth(),
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = { suggestionExpanded = true }) {
                                            Icon(
                                                Icons.Default.ArrowDropDown,
                                                contentDescription = "Dropdown"
                                            )
                                        }
                                    }
                                )

                                DropdownMenu(
                                    expanded = suggestionExpanded,
                                    onDismissRequest = { suggestionExpanded = false }
                                ) {
                                    exerciseSuggestions.forEach { suggestion ->
                                        DropdownMenuItem(
                                            text = { Text("${suggestion.first} - ${suggestion.second} ${suggestion.third}") },
                                            onClick = {
                                                selectedSuggestion = suggestion
                                                suggestionExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // If "Other" is selected, allow custom input
                            if (selectedSuggestion?.first == "Other" || selectedSuggestion == null) {
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    label = { Text("Exercise Name") },
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = duration,
                                    onValueChange = {
                                        duration = it.filter { c -> c.isDigit() }
                                    },
                                    label = { Text("Duration (min)") },
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = unit,
                                    onValueChange = { unit = it },
                                    label = { Text("Unit") },
                                    singleLine = true
                                )
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val newId = (exercisesList.maxOfOrNull { it.id } ?: 0) + 1
                            val newExercise = ExerciseItem(
                                id = newId,
                                name = name,
                                duration = duration.toIntOrNull() ?: 0,
                                unit = unit,
                                day = currentDay,
                                userId = userId ?: 0
                            )
                            exercisesList = (exercisesList + newExercise).toMutableList()
                            showAddDialog = false
                        }) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

