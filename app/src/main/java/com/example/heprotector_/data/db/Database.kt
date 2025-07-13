package com.example.heprotector_.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.heprotector_.data.db.converters.Converters
import com.example.heprotector_.data.db.dao.ExerciseSuggestionDao
import com.example.heprotector_.data.db.dao.HealthRecordDao
import com.example.heprotector_.data.db.dao.LocalFoodDao
import com.example.heprotector_.data.db.dao.MedicationScheduleDao
import com.example.heprotector_.data.db.dao.ReminderScheduleDao
import com.example.heprotector_.data.db.dao.UserDao
import com.example.heprotector_.data.db.entities.Exercise_Suggestion
import com.example.heprotector_.data.db.entities.Health_Record
import com.example.heprotector_.data.db.entities.Local_Food
import com.example.heprotector_.data.db.entities.Medication_schedule
import com.example.heprotector_.data.db.entities.Reminder_schedule
import com.example.heprotector_.data.db.entities.User_Account

@Database(
    entities = [
        User_Account::class,
        Health_Record::class,
        Medication_schedule::class,
        Reminder_schedule::class,
        Exercise_Suggestion::class,
        Local_Food::class
    ],
    version = 1,
    exportSchema = false // Set to true if you need to export schema for migrations
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun healthRecordDao(): HealthRecordDao
    abstract fun medicationScheduleDao(): MedicationScheduleDao
    abstract fun reminderScheduleDao(): ReminderScheduleDao
    abstract fun exerciseSuggestionDao(): ExerciseSuggestionDao
    abstract fun localFoodDao(): LocalFoodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "personal_health_db" // Name of your database file
                )
                    // .fallbackToDestructiveMigration() // Use with caution in production!
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}