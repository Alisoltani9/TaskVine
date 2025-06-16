package soltani.code.taskvine.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import soltani.code.taskvine.R


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskTitle = intent.getStringExtra("task_title") ?: return
        val taskId = intent.getIntExtra("task_id", 0)


        val sharedPreferences = context.getSharedPreferences("reminderSettings", Context.MODE_PRIVATE)
        val isReminderEnabled = sharedPreferences.getBoolean("isReminderEnabled", true)

        // Log for debugging
        Log.d("AlarmReceiver", "Reminder enabled: $isReminderEnabled")

        if (!isReminderEnabled) {
            Log.d("AlarmReceiver", "Reminders are disabled. Exiting without showing notification.")
            return
        }

        // Create an intent for the dismiss action
        val dismissIntent = Intent(context, NotificationDismissReceiver::class.java).apply {
            putExtra("notification_id", taskId)
        }

        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            taskId,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification with dismiss action
        val notification = NotificationCompat.Builder(context, "task_reminder_channel")
            .setSmallIcon(R.drawable.notification_widget)  // Set your small icon
            .setContentTitle("TaskVine")
            .setContentText(taskTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_dismiss,  // Icon for the dismiss button
                "Dismiss",  // Text for the dismiss button
                dismissPendingIntent  // PendingIntent triggered on button click
            )
            .build()

        // Show the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(taskId, notification)
    }
}
