package com.example.heprotector_.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heprotector_.data.db.entities.Exercise_Suggestion
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseSuggestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSuggestion(suggestion: Exercise_Suggestion)

    @Query("SELECT * FROM exercise_suggestions WHERE recordId = :recordId ORDER BY suggestedDate DESC")
    fun getExerciseSuggestionsForRecord(recordId: Int): Flow<List<Exercise_Suggestion>>

    @Query("SELECT * FROM exercise_suggestions WHERE recordId IN (SELECT recordId FROM health_records WHERE userId = :userId ORDER BY dateRecorded DESC LIMIT 5) ORDER BY suggestedDate DESC")
    fun getRecentExerciseSuggestionsForUser(userId: Int): Flow<List<Exercise_Suggestion>>
}