package com.example.heprotector.viewmodel.modelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heprotector.viewmodel.WeightViewModel

class WeightViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeightViewModel::class.java)) {
            return WeightViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
