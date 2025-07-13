package com.example.heprotector_.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.heprotector_.R // You'll need to add a small icon to your drawable folder
import com.example.heprotector_.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ReminderBroadcastReceiver : BroadcastReceiver() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        val reminderId = intent?.getIntExtra("REMINDER_ID", -1) ?: -1

        if (reminderId != -1) {
            scope.launch {
                val db = AppDatabase.getDatabase(context)
                val reminder = db.reminderScheduleDao().getReminderById(reminderId)

                reminder?.let {
                    if (it.isActive) { // Only show if reminder is active
                        showNotification(context, it.reminderType)
                        // If it's a one-time reminder, you might deactivate it here
                        // it.copy(isActive = false)
                        // db.reminderScheduleDao().updateReminderSchedule(it)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(context: Context, reminderType: String) {
        val channelId = "personal_health_tracker_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "Health Reminders"
        val descriptionText = "Reminders for tracking your health data and medications."
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with a proper icon
            .setContentTitle("Health Reminder!")
            .setContentText("Time to track your $reminderType.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Dismiss after tap
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification) // Unique ID for each notification
    }
}
