package com.example.heprotector.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heprotector.data.dao.MealDao
import com.example.heprotector.model.MealItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class MealViewModel(
    private val dao: MealDao,
    private val userId: Int
) : ViewModel() {

    private val _meals = MutableStateFlow<List<MealItem>>(emptyList())
    val meals: StateFlow<List<MealItem>> = _meals

    private val _currentDay = MutableStateFlow("MO")
    val currentDay: StateFlow<String> = _currentDay

    fun loadMeals(day: String) {
        viewModelScope.launch {
            _currentDay.value = day
            dao.getMealsByUserAndDay(userId, day).collect { _meals.value = it }
        }
    }

    fun saveMeals(userId: Int, day: String, meals: List<MealItem>) = viewModelScope.launch {
        dao.deleteMealsByUserAndDay(userId, day)

        val mealEntities = meals.map {
            MealItem(
                userId = userId,
                day = day,
                name = it.name,
                kcal = it.kcal,
                unit = it.unit
            )
        }

        dao.insertMeals(mealEntities)
    }

    fun deleteMeal(meal: MealItem) = viewModelScope.launch {
        dao.deleteMeal(meal)
    }
}
