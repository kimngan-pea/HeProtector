package com.example.heprotector.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heprotector.data.dao.BloodSugarDao

class BloodSugarViewModelFactory(
    private val dao: BloodSugarDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BloodSugarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BloodSugarViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
