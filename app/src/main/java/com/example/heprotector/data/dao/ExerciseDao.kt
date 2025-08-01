package com.example.heprotector.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heprotector.data.model.ExerciseItem
import kotlinx.coroutines.flow.Flow
import androidx.room.Delete

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseItem>)

    @Query("DELETE FROM exercise_table WHERE userId = :userId AND day = :day")
    suspend fun deleteExercisesByUserAndDay(userId: Int, day: String)

    @Query("SELECT * FROM exercise_table WHERE userId = :userId AND day = :day")
    fun getExercisesByUserAndDay(userId: Int, day: String): Flow<List<ExerciseItem>>

    @Delete
    suspend fun deleteExercise(item: ExerciseItem)

}
