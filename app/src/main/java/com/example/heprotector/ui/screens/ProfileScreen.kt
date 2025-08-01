package com.example.heprotector.ui.screens


import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.heprotector.ui.utils.calculateBMI
import com.example.heprotector.data.model.UserProfile
import com.example.heprotector.viewmodel.UserProfileViewModel
import com.example.heprotector.viewmodel.UserProfileViewModelFactory
import com.example.heprotector.viewmodel.WeightViewModel
import com.example.heprotector.viewmodel.modelFactory.WeightViewModelFactory
import com.example.heprotector.data.model.Weight
import com.example.heprotector.ui.theme.TealA700
import com.example.heprotector.ui.theme.greenGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: Int,
    navController: NavController,
    onBackClick: () -> Unit = {}
) {
    val orangeBar = Color(0xFFFF7043)
    val bmiBg = Color(0xFF81D4FA)



    var isEditing by remember { mutableStateOf(false) }

    var username by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var diabetesLevel by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }



    val context = LocalContext.current
    val userProfileViewModel: UserProfileViewModel = viewModel(
        factory = UserProfileViewModelFactory(
            context.applicationContext as Application
        )
    )



    val weightViewModel: WeightViewModel = viewModel(
        factory = WeightViewModelFactory(context.applicationContext as Application)
    )
    val profile by userProfileViewModel.profile.observeAsState()

    //  call once setUserId when userId change
    LaunchedEffect(userId) {
        userProfileViewModel.setUserId(userId)
    }
    //calculate BMI
    val weightBMI = profile?.weight ?: 0f
    val heightBMI = profile?.height ?: 0f

    val bmiFormatted = if (weightBMI != null && heightBMI != null && heightBMI > 0f) {
        val bmi = calculateBMI(weightBMI, heightBMI)
        String.format("%.2f", bmi)
    } else {
        "___"
    }

    LaunchedEffect(profile) {
        println(">> ProfileScreen: userProfile = $profile")
        profile?.let {
            username = it.username.orEmpty()
            fullName = it.fullName.orEmpty()
            address = it.address.orEmpty()
            diabetesLevel = it.diabetesType.orEmpty()
            height = it.height.toString()
            weight = it.weight.toString()
        }
    }



    println(">> Composable: Profile = $profile")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("User Profile", color = Color.Black, fontSize = 36.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                actions = {
                    Button(
                        onClick = { navController.navigate("login") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Logout", color = Color.Black, fontSize = 16.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TealA700)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        if (isEditing) {
                            val newProfile = (profile ?: UserProfile(
                                userId = userId,
                                username = username,
                                fullName = fullName,
                                address = address,
                                diabetesType = diabetesLevel,
                                height = height.filter { it.isDigit() || it == '.' }.toFloatOrNull()
                                    ?: 0f,
                                weight = weight.filter { it.isDigit() || it == '.' }.toFloatOrNull()
                                    ?: 0f,
                                goalWeight = 0f,
                                management = "",
                                dateOfBirth = 0L,
                                gender = "",
                                diagnosisMonth = 0,
                                diagnosisYear = 0
                            )).copy(
                                username = username,
                                fullName = fullName,
                                address = address,
                                diabetesType = diabetesLevel,
                                height = height.filter { it.isDigit() || it == '.' }.toFloatOrNull() ?: 0f,
                                weight = weight.filter { it.isDigit() || it == '.' }.toFloatOrNull() ?: 0f
                            )


                            userProfileViewModel.saveProfile(newProfile)

                            val newWeight = weight.filter { it.isDigit() || it == '.' }.toFloatOrNull() ?: 0f

                            weightViewModel.addEntry(
                                Weight(value = newWeight, userId = userId, time = System.currentTimeMillis())
                            )
                        }
                        isEditing = !isEditing },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = greenGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isEditing) "Save" else "Update Profile", color = Color.White, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))


            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column {
                    Text("User Information", fontSize = 20.sp, color = Color(0xFF00796B))
                    EditableInfoItem("Username", username, isEditing) { username = it }
                    EditableInfoItem("Full Name", fullName, isEditing) { fullName = it }
                    EditableInfoItem("Address", address, isEditing) { address = it }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(orangeBar)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Self-managed users", color = Color.White, fontSize = 24.sp)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column {
                    Text("Health Information", fontSize = 20.sp, color = Color(0xFF00796B))
                    EditableInfoItem(
                        "Diabetes Level",
                        diabetesLevel,
                        isEditing
                    ) { diabetesLevel = it }
                    EditableInfoItem("Height (cm)", height, isEditing) { height = it }
                    EditableInfoItem("Weight (kg)", weight, isEditing) { weight = it }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bmiBg)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "BMI",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(bmiFormatted, fontSize = 20.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun EditableInfoItem(label: String, value: String, isEditing: Boolean, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Text(text = value, fontSize = 16.sp)
            }
        }
    }
}
