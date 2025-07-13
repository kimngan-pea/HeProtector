package com.example.heprotector_

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.heprotector_.ui.theme.HeProtector_Theme
import com.example.heprotector_.ui.viewmodel.AppViewModelFactory
import com.example.heprotector_.viewmodel.AuthViewModel
import com.example.heprotector_.viewmodel.SharedViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeProtector_Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Use the custom ViewModelFactory
                    val authViewModel: AuthViewModel = viewModel(factory = AppViewModelFactory(
                        application
                    )
                    )
                    val sharedViewModel: SharedViewModel = viewModel(factory = AppViewModelFactory(application))

                    // Our main composable that handles navigation and initial checks
                    HeProtectorApp(
                        authViewModel = authViewModel,
                        sharedViewModel = sharedViewModel
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun HeProtectorAppTheme(function: @Composable () -> Unit) {
//    HeProtector_Theme {
//    }
//}