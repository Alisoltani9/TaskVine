package soltani.code.taskvine.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soltani.code.taskvine.R
import soltani.code.taskvine.helpers.ExpandableText
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.helpers.isReminderInPast
import soltani.code.taskvine.helpers.isReminderToday
import soltani.code.taskvine.helpers.isReminderUpcoming
import soltani.code.taskvine.model.Task
import soltani.code.taskvine.ui.screens.dialogs.EditTaskDialog
import soltani.code.taskvine.ui.theme.BrunswickGreen
import soltani.code.taskvine.viewmodel.CategoryViewModel
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun TaskItemLight(
    task: Task,
    categoryViewModel: CategoryViewModel,
    onCompleted: () -> Unit,
    onTaskDeleted: () -> Unit,
    onTaskUpdated: (Task) -> Unit  // Callback for updating the task after editing
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }


    val categoryList by categoryViewModel.categoryList.observeAsState(emptyList())

    // Task state for editing
    var editedTitle by remember { mutableStateOf(task.titleTask) }
    var editedDescription by remember { mutableStateOf(task.descriptionTask) }
    var editedCategory by remember { mutableStateOf(task.categoryTask) }
    var editedReminderTime by remember { mutableStateOf(task.reminderTime) }

    val colors = LocalCustomColors.current

    // Store initial values to revert on cancel
    val initialTitle = task.titleTask
    val initialDescription = task.descriptionTask
    val initialCategory = task.categoryTask
    val initialReminderTime = task.reminderTime

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colors.SecondaryBackground)
    ) {
        TaskContent(task, onCompleted, { showDeleteDialog = true },
            {
                showEditDialog = true
                // Update editable fields with the latest task data each time the dialog opens
                editedTitle = task.titleTask
                editedDescription = task.descriptionTask
                editedCategory = task.categoryTask
                editedReminderTime = task.reminderTime
            }
        )

        if (showDeleteDialog) {
            DeleteTaskDialog(
                onConfirm = {
                    onTaskDeleted()
                    showDeleteDialog = false
                },
                onDismiss = { showDeleteDialog = false }
            )
        }
        if (showEditDialog) {
            editedCategory?.let {
                EditTaskDialog(
                    isDialogOpen = showEditDialog,
                    onDismiss = {
                        // Reset to initial values if canceled
                        editedTitle = initialTitle
                        editedDescription = initialDescription
                        editedCategory = initialCategory
                        editedReminderTime = initialReminderTime

                        showEditDialog = false
                    },

                    onDeleteCategory = { category ->
                        categoryViewModel.deleteCategory(category)
                        editedCategory = "No Category"

                        // Call the update callback with the edited task data
                        val updatedTask = task.copy(

                            categoryTask = "No Category",
                        )
                        Log.d("TaskItem", "Updated task: $updatedTask") // Log to check

                        onTaskUpdated(updatedTask)
                    },
                    onAddCategoryClick = { taskCategory ->
                        if (taskCategory.isNotEmpty()) {
                            categoryViewModel.addCategory(taskCategory)
                        }
                    },
                    taskTitle = editedTitle,
                    taskDescription = editedDescription,
                    taskCategory = editedCategory ?: "",
                    taskReminder = editedReminderTime,
                    onTaskTitleChange = { newTitle -> editedTitle = newTitle },
                    onTaskDescriptionChange = { newDescription ->
                        editedDescription = newDescription
                    },
                    onTaskCategoryChange = { newCategory -> editedCategory = newCategory },
                    onReminderDateChange = { newReminder -> editedReminderTime = newReminder },
                    categoryList = categoryList.map { it.name }, // Passing the category names
                    onSaveTask = {
                        // Call the update callback with the edited task data
                        val updatedTask = task.copy(
                            titleTask = editedTitle,
                            descriptionTask = editedDescription,
                            categoryTask = editedCategory,
                            reminderTime = editedReminderTime
                        )
                        Log.d("TaskItem", "Updated task: $updatedTask") // Log to check

                        onTaskUpdated(updatedTask)
                        showEditDialog = false  // Close dialog after saving
                    }
                )
            }
        }


        ExpandableText(
            text = task.descriptionTask,
            fontSize = 12.sp,
            color = colors.primaryText,
            modifier = Modifier.padding(8.dp)
        )

        TaskReminderInfo(task)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun TaskContent(
    task: Task,
    onCompleted: () -> Unit,
    onShowDeleteDialog: () -> Unit,
    onShowEditDialog: () -> Unit
) {
    val colors = LocalCustomColors.current

    Row(
        modifier = Modifier.padding(top = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!task.completedTask) {
            RadioButton(
                selected = false,
                onClick = onCompleted,
                colors = RadioButtonDefaults.colors(
                    selectedColor = colors.primaryText,
                    unselectedColor = colors.primaryText
                )
            )
            Text(
                text = task.titleTask,
                fontSize = 18.sp,
                color = colors.primaryText,
                modifier = Modifier.weight(1f)
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = if (task.categoryTask == "Category" || task.categoryTask == "No Category") {
                    ""
                } else {
                    task.categoryTask ?: ""
                },
                fontSize = 12.sp,
                color = colors.primaryText
            )
            IconButton(onClick = onShowEditDialog) {
                Icon(
                    painter = painterResource(id = R.drawable.rounded_edit_square_24),
                    contentDescription = "Edit Icon",
                    tint = colors.primaryText,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Text(
                text = task.titleTask,
                fontSize = 18.sp,
                color = colors.primaryText,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            IconButton(onClick = onShowDeleteDialog) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Icon",
                    tint = BrunswickGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun DeleteTaskDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    val colors = LocalCustomColors.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.SecondaryBackground,
        title = {
            Text(
                text = "Delete Task",
                color = colors.primaryText,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete this task?",
                color = colors.primaryText,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
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

@Composable
private fun TaskReminderInfo(task: Task) {
    val colors = LocalCustomColors.current

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = getReminderText(task),
            fontSize = 14.sp,
            color = when {
                task.reminderTime != null && isReminderInPast(task.reminderTime) || task.completedTask -> colors.secondaryText // Dynamic text color for past reminder or completed task
                else -> colors.primaryText // Dynamic text color for active reminder
            },
            fontStyle = if (task.reminderTime != null && isReminderInPast(task.reminderTime) || task.completedTask) FontStyle.Italic else FontStyle.Normal
        )

        Spacer(modifier = Modifier.weight(1f))

        if (task.reminderTime != null) {
            Text(
                text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(task.reminderTime),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primaryText
            )
            Icon(
                imageVector = Icons.Default.Alarm,
                contentDescription = "Alarm Icon",
                tint = colors.primaryText,
                modifier = Modifier
                    .size(18.dp)
                    .padding(top = 4.dp)
            )
        }
    }
}

private fun getReminderText(task: Task): String {
    return when {
        task.completedTask -> SimpleDateFormat(
            "MMMM d",
            Locale.getDefault()
        ).format(task.reminderTime ?: task.created_at)

        task.reminderTime != null && isReminderToday(task.reminderTime) -> "Today"
        task.reminderTime != null && isReminderInPast(task.reminderTime) -> SimpleDateFormat(
            "MMMM d",
            Locale.getDefault()
        ).format(task.reminderTime) + " (Past)"

        task.reminderTime != null && isReminderUpcoming(task.reminderTime) -> SimpleDateFormat(
            "MMMM d",
            Locale.getDefault()
        ).format(task.reminderTime)

        else -> "Anytime"
    }
}



