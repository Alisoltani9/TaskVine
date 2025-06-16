package soltani.code.taskvine

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import soltani.code.taskvine.model.DatabaseInitializer
import javax.inject.Inject

@HiltAndroidApp
class TaskApp : Application() {

    @Inject
    lateinit var databaseInitializer: DatabaseInitializer

    companion object {
        private const val CHANNEL_ID = "task_reminder_channel"
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize the database with default data
        CoroutineScope(Dispatchers.IO).launch {
            databaseInitializer.initializeDatabase()
        }

        // Create notification channel
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Task Reminder"
            val descriptionText = "Channel for task reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            // Log to verify channel creation
            Log.d("TaskApp", "Notification channel created: $CHANNEL_ID")
        }
    }
}
