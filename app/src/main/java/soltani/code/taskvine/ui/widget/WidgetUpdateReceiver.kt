package soltani.code.taskvine.ui.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WidgetUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "soltani.code.taskvine.WIDGET_UPDATE") {
            CoroutineScope(Dispatchers.IO).launch {
                val manager = GlanceAppWidgetManager(context)
                val glanceIds = manager.getGlanceIds(HelloWorldWidget::class.java)

                glanceIds.forEach { glanceId ->
                    HelloWorldWidget().update(context, glanceId)
                }
            }
        }
    }
}
