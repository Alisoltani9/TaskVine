package soltani.code.taskvine.ui.widget

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RefreshWidgetAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.d("WidgetUpdate", "Refresh button clicked")

        withContext(Dispatchers.IO) { // Ensure it runs on IO dispatcher
            val manager = GlanceAppWidgetManager(context)
            val glanceIds = manager.getGlanceIds(HelloWorldWidget::class.java)

            glanceIds.forEach { id ->
                HelloWorldWidget().update(context, id)
            }
        }
    }
}
