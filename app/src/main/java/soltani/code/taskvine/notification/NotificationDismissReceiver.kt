package soltani.code.taskvine.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Get the notification ID from the intent
        val notificationId = intent.getIntExtra("notification_id", 0)

        // Cancel the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)

        // Optional: Show a toast message when dismissed
        Toast.makeText(context, "Reminder dismissed", Toast.LENGTH_SHORT).show()
    }
}
