package soltani.code.taskvine.ui.screens.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun AchievementDialog(dialogTitle: String?, onDismiss: () -> Unit) {

    val colors = LocalCustomColors.current

    if (!dialogTitle.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Congratulations on unlocking this achievement!",
                    style = MaterialTheme.typography.titleMedium,  // Use a more minimal font style
                    color = colors.thirdText // Ensure text is visible on dark/light themes
                )
            },
            text = {
                Text(
                    text = "$dialogTitle has been earned!",
                    style = MaterialTheme.typography.bodyMedium,  // Simple text style
                    color = colors.thirdText // Slightly muted color for description
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(contentColor = colors.primaryText)  // Primary button color
                ) {
                    Text("Got it")
                }
            },
            containerColor = colors.primaryBackground,  // Use surface variant color for a clean background
            shape = MaterialTheme.shapes.medium,  // Rounded corners for a modern look
            modifier = Modifier.padding(16.dp) // Add padding around the dialog content for spacing
        )
    }
}
