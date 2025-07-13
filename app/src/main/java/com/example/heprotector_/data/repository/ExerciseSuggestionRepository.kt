package com.example.heprotector_.data.repository

import com.example.heprotector_.data.db.dao.ExerciseSuggestionDao
import com.example.heprotector_.data.db.entities.Exercise_Suggestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ExerciseSuggestionRepository(private val exerciseSuggestionDao: ExerciseSuggestionDao) {

    /**
     * Inserts a new exercise suggestion.
     * @param suggestion The Exercise_Suggestion object to insert.
     */
    suspend fun insertExerciseSuggestion(suggestion: Exercise_Suggestion) =
        withContext(Dispatchers.IO) {
            exerciseSuggestionDao.insertExerciseSuggestion(suggestion)
        }

    /**
     * Retrieves exercise suggestions linked to a specific health record.
     * @param recordId The ID of the health record.
     * @return A Flow emitting a list of Exercise_Suggestion objects.
     */
    fun getExerciseSuggestionsForRecord(recordId: Int): Flow<List<Exercise_Suggestion>> {
        return exerciseSuggestionDao.getExerciseSuggestionsForRecord(recordId)
    }

    /**
     * Retrieves recent exercise suggestions for a specific user, typically based on their latest health records.
     * This relies on the DAO query to select based on the last few records.
     * @param userId The ID of the user.
     * @return A Flow emitting a list of Exercise_Suggestion objects.
     */
    fun getRecentExerciseSuggestionsForUser(userId: Int): Flow<List<Exercise_Suggestion>> {
        return exerciseSuggestionDao.getRecentExerciseSuggestionsForUser(userId)
    }
}