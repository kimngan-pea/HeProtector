import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.data.model.ReminderItem
import com.example.heprotector.ui.theme.*
import com.example.heprotector.ui.utils.getTodayShortDay
import com.example.heprotector.viewmodel.ReminderViewModel
import com.example.heprotector.viewmodel.ReminderViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleReminderScreen(
    onBackClick: () -> Unit = {},
    userId: Int?
) {
    var selectedDay by remember { mutableStateOf(getTodayShortDay()) }
    var expandedMetric by remember { mutableStateOf(false) }
    var selectedMetric by remember { mutableStateOf("Blood Sugar") }

    val context = LocalContext.current
    val reminderDao = AppDatabase.getInstance(context).reminderDao()
    val factory = remember(userId) { ReminderViewModelFactory(reminderDao, userId ?: 0) }
    val reminderViewModel: ReminderViewModel = viewModel(factory = factory)

    val remindersState = reminderViewModel.getReminders().collectAsState(initial = emptyList())
    val reminders = remindersState.value

    val currentReminders by remember(reminders, selectedDay, selectedMetric) {
        derivedStateOf {
            reminders.filter {
                it.type == selectedMetric && it.day == selectedDay
            }
        }
    }

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Schedule Reminder",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TealA700),
                modifier = Modifier.shadow(4.dp)
            )
        }
    ) { innerPadding ->
        Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
            Spacer(modifier = Modifier.height(12.dp))

            Box {
                Text(
                    text = selectedMetric,
                    modifier = Modifier
                        .clickable { expandedMetric = true }
                        .padding(8.dp)
                        .background(greenGray, RoundedCornerShape(4.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
                DropdownMenu(
                    expanded = expandedMetric,
                    onDismissRequest = { expandedMetric = false }
                ) {
                    listOf("Blood Sugar", "Weight", "HbA1c").forEach { metric ->
                        DropdownMenuItem(
                            text = { Text(metric, fontSize = 26.sp) },
                            onClick = {
                                selectedMetric = metric
                                expandedMetric = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            DayTabRow(
                selectedDay = selectedDay,
                onDaySelected = { selectedDay = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (reminders.isEmpty() && currentReminders.isEmpty()) {
                Text("No reminders found", fontSize = 20.sp)
            } else {
                ReminderList(reminders = currentReminders, reminderViewModel)
            }

            if (showAddDialog) {
                AddReminderDialog(
                    selectedMetric = selectedMetric,
                    selectedDay = selectedDay,
                    userId = userId ?: 0,
                    onDismiss = { showAddDialog = false },
                    onSave = { reminder ->
                        reminderViewModel.saveReminder(reminder)
                        showAddDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun AddReminderDialog(
    selectedMetric: String,
    selectedDay: String,
    userId: Int,
    onDismiss: () -> Unit,
    onSave: (ReminderItem) -> Unit
) {
    var period by remember { mutableStateOf("Morning") }
    var option by remember { mutableStateOf("Before") }
    var hour by remember { mutableStateOf(7) }
    var minute by remember { mutableStateOf(30) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val newReminder = ReminderItem(
                    userId = userId,
                    type = selectedMetric,
                    day = selectedDay,
                    period = period,
                    beforeAfter = option, // "Before" or "After"
                    hour = hour,
                    minute = minute
                )
                onSave(newReminder)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Reminder Time") },
        text = {
            Column {
                Text("Select Period:", fontSize = 20.sp)
                DropdownSelector(
                    value = period,
                    options = listOf("Morning", "Afternoon", "Evening"),
                    onValueChange = { selected -> period = selected }
                )

                Spacer(Modifier.height(8.dp))

                Text("Before / After:", fontSize = 20.sp)
                Row(
                    modifier = Modifier
                        .background(LavenderLight, shape = RoundedCornerShape(50))
                        .padding(4.dp)
                ) {
                    listOf("Before", "After").forEach {
                        Box(
                            modifier = Modifier
                                .background(
                                    if (option == it) Lavender else Color.Transparent,
                                    shape = RoundedCornerShape(50)
                                )
                                .clickable { option = it }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(it, fontSize = 16.sp)
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text("Select Time:", fontSize = 20.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    DropdownSelector(
                        value = hour,
                        options = (1..12).toList(),
                        onValueChange = { hour = it },
                        label = "Hour",
                        modifier = Modifier.weight(1f),
                    )

                    Text(":", fontSize = 26.sp, modifier = Modifier.padding(horizontal = 4.dp))

                    DropdownSelector(
                        value = minute,
                        options = listOf(0, 15, 30, 45),
                        onValueChange = { minute = it },
                        label = "Minute",
                        modifier = Modifier.weight(1f)
                    )
                }

            }
        }
    )
}

@Composable
fun ReminderList(reminders: List<ReminderItem>, reminderViewModel: ReminderViewModel) {
    LazyColumn {
        items(reminders, key = { it.id }) { reminder ->
            ReminderTimeBlock(
                id = reminder.id,
                period = reminder.period,
                selectedOption = reminder.beforeAfter,
                hour = reminder.hour,
                minute = reminder.minute,
                userId = reminder.userId ?: 0,
                metric = reminder.type,
                day = reminder.day,
                onUpdate = { reminderViewModel.updateReminder(it) },
                onDelete = { reminderViewModel.deleteReminder(it) }
            )
        }
    }
}

@Composable
fun ReminderTimeBlock(
    id: Int,
    period: String,
    selectedOption: String,
    hour: Int,
    minute: Int,
    userId: Int,
    metric: String,
    day: String,
    onUpdate: (ReminderItem) -> Unit,
    onDelete: (ReminderItem) -> Unit
) {
    val options = listOf("Before", "After")
    val periods = listOf("Morning", "Afternoon", "Evening")
    val isUnset = hour == 0 && minute == 0

    var localOption by remember { mutableStateOf(selectedOption) }
    var localPeriod by remember { mutableStateOf(period) }
    var localHour by remember { mutableStateOf(hour) }
    var localMinute by remember { mutableStateOf(minute) }

    var expanded by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }
    val blockAlpha = if (isUnset && !isSaved) 0.4f else 1f
    var showConfirmDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                if (isSaved) Color.Green.copy(alpha = 0.5f)
                else if (isUnset) Color.LightGray.copy(alpha = 0.3f)
                else Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .alpha(blockAlpha)
            .padding(12.dp)
    ) {
        Text(
            text = "Period: $localPeriod",
            fontWeight = FontWeight.Bold,
            color = BlueGray,
            fontSize = 20.sp,
            modifier = Modifier.clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            periods.forEach { period ->
                DropdownMenuItem(
                    text = { Text(period, fontSize = 18.sp) },
                    onClick = {
                        localPeriod = period
                        expanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .background(LavenderLight, shape = RoundedCornerShape(50))
                    .padding(4.dp)
            ) {
                options.forEach { option ->
                    Box(
                        modifier = Modifier
                            .background(
                                if (option == localOption) Lavender else Color.Transparent,
                                shape = RoundedCornerShape(50)
                            )
                            .clickable {
                                localOption = option
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(option, fontSize = 20.sp)
                    }
                }

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ){
                DropdownSelector(
                    value = localHour,
                    options = (0..12).toList(),
                    onValueChange = { localHour = it },
                    label = "Hour",
                    modifier = Modifier.weight(1f)
                )
                Text(":", fontSize = 26.sp, modifier = Modifier.padding(horizontal = 4.dp))
                DropdownSelector(
                    value = localMinute,
                    options = listOf(0, 15, 30, 45),
                    onValueChange = { localMinute = it },
                    label = "Minute",
                    modifier = Modifier.weight(1f)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            isSaved = true
                            val newReminder = ReminderItem(
                                id = id,
                                userId = userId,
                                type = metric,
                                day = day,
                                period = localPeriod,
                                beforeAfter = localOption,
                                hour = localHour,
                                minute = localMinute
                            )
                            Log.d("DEBUG", "ReminderTimeBlock, Update reminder: $newReminder")
                            onUpdate(newReminder)
                        },
                        modifier = Modifier.height(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBlue),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text("OK", fontSize = 18.sp, color = Color.White)
                    }
                    Button(
                        onClick = {
                            showConfirmDialog = true
                        },
                        modifier = Modifier.height(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text("X", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Confirm delete") },
                text = { Text("Are you sure to delete?") },
                confirmButton = {
                    TextButton(onClick = {
                        val reminderToDelete = ReminderItem(
                            id = id,
                            userId = userId,
                            type = metric,
                            day = day,
                            period = localPeriod,
                            beforeAfter = localOption,
                            hour = localHour,
                            minute = localMinute
                        )
                        onDelete(reminderToDelete)
                        showConfirmDialog = false
                    }) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showConfirmDialog = false
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun <T> DropdownSelector(
    value: T,
    options: List<T>,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize()) {
        Column {
            label?.let {
                Text(
                    text = it,
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .background(Color.White)
            ) {
                Text(text = value.toString(), fontSize = 18.sp)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(it.toString()) },
                        onClick = {
                            onValueChange(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
