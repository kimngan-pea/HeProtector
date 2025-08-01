package com.example.heprotector.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heprotector.data.dao.ExerciseDao
import com.example.heprotector.data.model.ExerciseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val dao: ExerciseDao,
    private val userId: Int
) : ViewModel() {

    fun getExercisesForDay(day: String): Flow<List<ExerciseItem>> {
        return dao.getExercisesByUserAndDay(userId, day)
    }

    fun saveExercises(day: String, exercises: List<ExerciseItem>) = viewModelScope.launch {
        dao.deleteExercisesByUserAndDay(userId, day)
        dao.insertExercises(exercises)
    }

    fun deleteExercise(item: ExerciseItem) = viewModelScope.launch {
        dao.deleteExercise(item)
    }
}
