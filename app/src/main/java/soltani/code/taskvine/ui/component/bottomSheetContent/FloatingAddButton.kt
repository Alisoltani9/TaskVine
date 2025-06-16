package soltani.code.taskvine.ui.component.bottomSheetContent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun FloatingAddButton(onAddTaskClick: () -> Unit) {
    val colors = LocalCustomColors.current

    FloatingActionButton(
        onClick = onAddTaskClick,
        containerColor = colors.primaryText,
        contentColor = colors.cardBackground,
    ) {
        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Task")
    }
}