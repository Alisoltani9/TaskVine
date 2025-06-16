package soltani.code.taskvine.ui.widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import soltani.code.taskvine.R
import soltani.code.taskvine.model.Task
import soltani.code.taskvine.model.TaskDatabase
import java.text.SimpleDateFormat
import java.util.Locale

class HelloWorldWidget: GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Single
    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val taskDao = TaskDatabase.getInstance(context).getTaskDao() // Replace with your database instance

        val tasksForToday = withContext(Dispatchers.IO) {
            taskDao.getPendingTask(System.currentTimeMillis())
        }
        Log.d("WidgetUpdate1", "Widget content updated at ${System.currentTimeMillis()} with ${tasksForToday.size} tasks")
        provideContent{
            TaskVineContent(tasksForToday)
        }
        Log.d("WidgetUpdate1", "Widget content updated with ${tasksForToday.size} tasks")

    }
}

@Composable
private fun TaskVineContent(tasks: List<Task>) {
    GlanceTheme {
        Scaffold(
            backgroundColor = GlanceTheme.colors.widgetBackground,
        )
        {
            Column(
                modifier = GlanceModifier.fillMaxSize().padding(16.dp)
            ) {
                // Header Row
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.notificon),
                        contentDescription = "App Icon",
                        modifier = GlanceModifier.size(24.dp)
                    )
                    Spacer(modifier = GlanceModifier.width(8.dp))
                    Text(
                        text = "TaskVine",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())


                    CircleIconButton(
                        imageProvider = ImageProvider(R.drawable.baseline_refresh_24),
                        contentDescription = "Refresh",
                        onClick = actionRunCallback<RefreshWidgetAction>()
                    )


                }

                Spacer(modifier = GlanceModifier.height(20.dp))

                // List of items
                LazyColumn{
                    items(tasks) { task ->
                        Row(
                            modifier = GlanceModifier.fillMaxWidth()
                                .padding(8.dp) // Padding inside the row
                            ,
                            verticalAlignment = Alignment.CenterVertically
                        ) {


                            Text(
                                text = task.titleTask,
                                style = TextStyle(fontSize = 16.sp)
                            )
                            Spacer(modifier = GlanceModifier.defaultWeight())

                                Text(
                                    text = SimpleDateFormat("HH:mm, dd MMM", Locale.getDefault()).format(
                                        task.reminderTime
                                    ),
                                    style = TextStyle(fontSize = 11.sp)
                                )

                        }
                    }
                }
            }

        }

}


}

