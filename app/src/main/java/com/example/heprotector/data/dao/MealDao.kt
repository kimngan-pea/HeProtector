package com.example.heprotector.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heprotector.model.MealItem

import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM meals WHERE userId = :userId AND day = :day")
    fun getMealsByUserAndDay(userId: Int, day: String): Flow<List<MealItem>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMeal(meal: MealItem)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMeals(meals: List<MealItem>)

    @Delete
    suspend fun deleteMeal(meal: MealItem)

    @Query("DELETE FROM meals WHERE userId = :userId AND day = :day")
    suspend fun deleteMealsByUserAndDay(userId: Int, day: String)
}