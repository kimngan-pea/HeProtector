package com.example.heprotector.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heprotector.data.dao.ExerciseDao
import com.example.heprotector.viewmodel.ExerciseViewModel

class ExerciseViewModelFactory(
    private val dao: ExerciseDao,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
            return ExerciseViewModel(dao, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
