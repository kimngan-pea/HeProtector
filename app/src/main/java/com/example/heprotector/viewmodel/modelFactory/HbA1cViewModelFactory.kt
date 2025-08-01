package com.example.heprotector.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heprotector.data.dao.HbA1cDao

class HbA1cViewModelFactory(private val dao: HbA1cDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HbA1cViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HbA1cViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
