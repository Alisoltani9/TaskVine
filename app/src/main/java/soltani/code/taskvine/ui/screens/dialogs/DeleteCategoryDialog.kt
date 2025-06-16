package soltani.code.taskvine.ui.screens.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun DeleteCategoryDialog(
    categoryToDelete: String?,
    onDismiss: () -> Unit,
    onDeleteCategory: (String) -> Unit
) {
    val colors = LocalCustomColors.current

    AlertDialog(
        containerColor = colors.SecondaryBackground,
        onDismissRequest = onDismiss,
        title = { Text("Delete Category", color = colors.primaryText) },
        text = {
            Text(
                "Are you sure you want to delete the category \"$categoryToDelete\"?",
                color = colors.primaryText
            )
        }, // Primary color for text
        confirmButton = {
            TextButton(onClick = { categoryToDelete?.let { onDeleteCategory(it) }; onDismiss() }) {
                Text("Delete", color = colors.primaryText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = colors.primaryText)
            }
        }
    )
}