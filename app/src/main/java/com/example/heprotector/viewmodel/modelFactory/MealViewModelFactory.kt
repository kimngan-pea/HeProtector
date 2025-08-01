package com.example.heprotector.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heprotector.data.dao.MealDao

class MealViewModelFactory(
    private val mealDao: MealDao,
    private val userId: Int?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            val nonNullUserId = userId ?: throw IllegalArgumentException("UserId is required")
            return MealViewModel(mealDao, nonNullUserId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

