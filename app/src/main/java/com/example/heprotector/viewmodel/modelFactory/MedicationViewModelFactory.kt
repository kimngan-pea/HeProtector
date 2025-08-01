package com.example.heprotector.viewmodel.modelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heprotector.data.dao.MedicationDao
import com.example.heprotector.viewmodel.MedicationViewModel

class MedicationViewModelFactory(
    private val dao: MedicationDao,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicationViewModel::class.java)) {
            return MedicationViewModel(dao, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

