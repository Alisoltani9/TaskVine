package soltani.code.taskvine.ui.screens.dialogs.editTask

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.ui.screens.dialogs.AddCategoryDialog
import soltani.code.taskvine.ui.component.CustomDropdownMenuItem


@Composable
fun CategoryDropdown(
    categoryList: List<String>,
    onTaskCategoryChange: (String) -> Unit,
    onDeleteCategory: (String) -> Unit,
    onAddCategoryClick: (String) -> Unit, // Accept new category name

    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    val colors = LocalCustomColors.current

    var isDeleteCategoryDialogOpen by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<String?>(null) }
    var isAddCategoryDialogOpen by remember { mutableStateOf(false) }

    // Dialog for adding new category
    AddCategoryDialog(
        isDialogOpen = isAddCategoryDialogOpen,
        onDismiss = { isAddCategoryDialogOpen = false },
        onAddCategory = { newCategoryName ->
            onAddCategoryClick(newCategoryName) // Pass the new category name to the parent
            onTaskCategoryChange(newCategoryName) // Update taskCategory with new category
            isAddCategoryDialogOpen = false
        }
    )


    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onExpandedChange(false) },
        modifier = Modifier
            .width(150.dp)
            .background(colors.SecondaryBackground)
    ) {
        categoryList.forEach { category ->
            CustomDropdownMenuItem(
                text = {
                    Text(text = category, color = colors.primaryText)

                },
                onClick = {  // Handle normal click for category change
                    Log.d("CategoryDropdown", "onClick: Category $category tapped")
                    onTaskCategoryChange(category)
                    onExpandedChange(false)
                },
                onLongPress = {
                    if (category != "No Category") {
                        categoryToDelete = category  // Set the category to delete
                        isDeleteCategoryDialogOpen = true
                    }

                }
            )

        }

        // Show delete category dialog when long pressed
        if (isDeleteCategoryDialogOpen) {
            AlertDialog(
                containerColor = colors.SecondaryBackground,
                onDismissRequest = { isDeleteCategoryDialogOpen = false },
                title = {
                    Text(
                        text = "Delete Category",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.primaryText
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete the category \"${categoryToDelete}\"?",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.primaryText
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        categoryToDelete?.let { onDeleteCategory(it) }  // Call onDeleteCategory with selected category
                        isDeleteCategoryDialogOpen = false
                    }) {
                        Text(
                            text = "Delete",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.primaryText
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        Log.d("CategoryDropdown", "Delete action cancelled")
                        isDeleteCategoryDialogOpen = false
                    }) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.primaryText
                        )
                    }
                }
            )

        }

        // Add New Category Option
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = colors.highlight
                    )
                    Text("Add More", color = colors.highlight)
                }
            },
            onClick = {
                onExpandedChange(false)
                isAddCategoryDialogOpen = true
            }
        )
    }
}

