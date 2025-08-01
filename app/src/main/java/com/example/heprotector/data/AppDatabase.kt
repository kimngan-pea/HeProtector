package com.example.heprotector.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.heprotector.data.model.BloodSugar
import com.example.heprotector.data.dao.BloodSugarDao
import com.example.heprotector.data.dao.*
import com.example.heprotector.data.model.*
import com.example.heprotector.model.MealItem


@Database(entities = [
    BloodSugar::class,
    HbA1c::class,
    Weight::class,
    UserProfile::class,
    UserAccount::class,
    MedicationSchedule::class,
    ReminderItem::class,
    MealItem::class,
    ExerciseItem::class],
    version = 29)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bloodSugarDao(): BloodSugarDao
    abstract fun hba1cDao(): HbA1cDao
    abstract fun weightDao(): WeightDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun userAccountDao(): UserAccountDao
    abstract fun medicationDao(): MedicationDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun mealDao(): MealDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "heprotector_db"
                )
                    // automatic delete and create database if change
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
