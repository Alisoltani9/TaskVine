package soltani.code.taskvine.ui.screens.dialogs.editTask

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun ReminderButton(
    reminderText: String,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
) {
    val colors = LocalCustomColors.current

    Button(
        onClick = {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        },
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primaryText),
        border = BorderStroke(1.dp, colors.primaryText),
        modifier = Modifier.size(width = 120.dp, height = 40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
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
                reminderText,
                color = colors.primaryText,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.requiredSize(120.dp, 20.dp)
            )
        }
    }
}
