package soltani.code.taskvine.ui.component.bottomSheetContent

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.helpers.showDatePicker
import soltani.code.taskvine.helpers.showTimePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun ReminderButtonBs(
    reminderText: String,
    context: android.content.Context,
    coroutineScope: CoroutineScope,
    permissionLauncher: androidx.activity.compose.ManagedActivityResultLauncher<String, Boolean>,
    onReminderDateChange: (Long?) -> Unit,
    onReminderTextChange: (String) -> Unit

) {
    val colors = LocalCustomColors.current

    Button(
        onClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showDateTimePicker(
                    context,
                    coroutineScope,
                    onReminderDateChange,
                    onReminderTextChange
                )
            } else {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        },
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primaryText),
        border = BorderStroke(1.dp, colors.primaryText),
        modifier = Modifier.size(width = 120.dp, height = 40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Filled.Alarm,
                contentDescription = "Reminder Icon",
                tint = colors.primaryText,
                modifier = Modifier
                    .size(20.dp)
                    .offset((-15).dp)
            )
            Text(
                text = reminderText,
                color = colors.primaryText,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .requiredSize(120.dp, 20.dp)
            )
        }
    }
}

private fun showDateTimePicker(
    context: android.content.Context,
    coroutineScope: CoroutineScope,
    onReminderDateChange: (Long?) -> Unit,
    onReminderTextChange: (String) -> Unit
) {
    coroutineScope.launch {
        showDatePicker(
            context,
            onDateSelected = { date ->
                showTimePicker(context, date) { dateTime ->
                    onReminderDateChange(dateTime.time)
                    // Update the reminder text with formatted date and time
                    // Format date and time for display
                    val formattedDateTime = SimpleDateFormat(
                        "HH:mm, dd MMM", Locale.getDefault()
                    ).format(dateTime.time)

                    // Update the reminder text
                    onReminderTextChange(formattedDateTime)
                }
            },
            onClearReminder = {
                onReminderDateChange(null)
                onReminderTextChange("Reminder") // Reset the text to default if cleared
            }
        )
    }
}