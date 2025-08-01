package com.example.heprotector.ui.screens


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
import com.example.heprotector.model.MealItem
import com.example.heprotector.viewmodel.MealViewModel
import com.example.heprotector.viewmodel.MealViewModelFactory
import DayTabRow
import PlanList
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import com.example.heprotector.ui.theme.TealA700
import com.example.heprotector.ui.theme.greenGray
import removeAndResequenceIds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietPlanScreen(
    userId: Int?,
    onBackClick: () -> Unit
) {
    val selectedCity = remember { mutableStateOf("Ho Chi Minh city") }
    var currentDay by remember { mutableStateOf("MO") }

    // ViewModel setup
    val context = LocalContext.current
    val mealDao = AppDatabase.getInstance(context).mealDao()
    val factory = remember(userId) { MealViewModelFactory(mealDao, userId ?: 0) }
    val mealViewModel: MealViewModel = viewModel(factory = factory)
    var showAddDialog by remember { mutableStateOf(false) }

    // Load meals from Room when day changes
    LaunchedEffect(currentDay) {
        mealViewModel.loadMeals(currentDay)
    }
    println("mealViewModel: $mealViewModel")

    // Observe meals from ViewModel
    val savedMeals by mealViewModel.meals.collectAsState()
    var meals by remember(currentDay, savedMeals) {
        mutableStateOf(savedMeals.mapIndexed { index, meal ->
            MealItem(
                id = index + 1,
                name = meal.name,
                kcal = meal.kcal,
                unit = meal.unit,
                day = currentDay,
                userId = userId ?: 0
            )
        }.toMutableList())
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diet plan", fontSize = 36.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showAddDialog = true
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Meal")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = TealA700)

            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // location
            Text("City: ${selectedCity.value}", fontWeight = FontWeight.Bold, fontSize = 24.sp)

            // day
            DayTabRow(
                selectedDay = currentDay,
                onDaySelected = { selected ->
                    currentDay = selected
                }
            )

            //kcal
            val totalKcal = meals.sumOf { it.kcal }
            Text("Total: $totalKcal Kcal", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)


            //List meal
            PlanList(
                items = meals,
                onRemoveItem = { item ->
                    if (item is MealItem) {
                        meals = removeAndResequenceIds(
                            list = meals,
                            target = item,
                            getId = { it.id },
                            updateId = { oldItem, newId -> oldItem.copy(id = newId) }
                        ).toMutableList()
                    }
                },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    mealViewModel.saveMeals(
                        userId = userId ?: 0,
                        day = currentDay,
                        meals = meals
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = greenGray)
            ) {
                Text("Save", fontSize = 20.sp, color = Color.White)
            }

            if (showAddDialog) {
                val mealSuggestions = listOf(
                    Triple("Rice", 200, "Kcal"),
                    Triple("Salad", 100, "Kcal"),
                    Triple("Chicken", 250, "Kcal"),
                    Triple("Fruit", 90, "Kcal"),
                    Triple("Soup", 150, "Kcal"),
                    Triple("Other", 0, "Kcal")
                )

                var selectedSuggestion by remember {
                    mutableStateOf<Triple<String, Int, String>?>(
                        null
                    )
                }
                var name by remember { mutableStateOf("") }
                var kcal by remember { mutableStateOf("") }
                var unit by remember { mutableStateOf("Kcal") }

                var suggestionExpanded by remember { mutableStateOf(false) }

                // update meal suggestion
                LaunchedEffect(selectedSuggestion) {
                    selectedSuggestion?.let {
                        if (it.first != "Other") {
                            name = it.first
                            kcal = it.second.toString()
                            unit = it.third
                        } else {
                            name = ""
                            kcal = ""
                            unit = "Kcal"
                        }
                    }
                }

                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    title = { Text("Add New Meal") },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Dropdown suggest meal
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
                                    mealSuggestions.forEach { suggestion ->
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

                            // Option other
                            if (selectedSuggestion?.first == "Other" || selectedSuggestion == null) {
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    label = { Text("Meal Name") },
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = kcal,
                                    onValueChange = { kcal = it.filter { c -> c.isDigit() } },
                                    label = { Text("Calories (kcal)") },
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
                            val newId = (meals.maxOfOrNull { it.id } ?: 0) + 1
                            val newMeal = MealItem(
                                id = newId,
                                name = name,
                                kcal = kcal.toIntOrNull() ?: 0,
                                unit = unit,
                                day = currentDay,
                                userId = userId ?: 0
                            )
                            meals = (meals + newMeal).toMutableList()
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
