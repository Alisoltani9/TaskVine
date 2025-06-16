package soltani.code.taskvine.ui.screens.dialogs

import android.text.format.DateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.helpers.showDatePicker
import soltani.code.taskvine.helpers.showTimePicker
import soltani.code.taskvine.ui.screens.dialogs.editTask.CategoryButton
import soltani.code.taskvine.ui.screens.dialogs.editTask.CategoryDropdown
import soltani.code.taskvine.ui.screens.dialogs.editTask.ReminderButton
import soltani.code.taskvine.ui.screens.dialogs.editTask.TaskInputFields
import kotlinx.coroutines.launch

@Composable
fun EditTaskDialog(
    taskTitle: String,
    taskDescription: String,
    taskCategory: String,
    taskReminder: Long?,
    categoryList: List<String>,
    isDialogOpen: Boolean,
    onTaskTitleChange: (String) -> Unit,
    onTaskDescriptionChange: (String) -> Unit,
    onTaskCategoryChange: (String) -> Unit,
    onReminderDateChange: (Long?) -> Unit,
    onDeleteCategory: (String) -> Unit,
    onAddCategoryClick: (String) -> Unit,
    onSaveTask: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var reminderText by remember {
        mutableStateOf(taskReminder?.let { DateFormat.format("HH:mm, dd MMM", it).toString() }
            ?: "Reminder")
    }
    val colors = LocalCustomColors.current

    // Permission launcher for notifications
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                coroutineScope.launch {
                    showDatePicker(context,
                        onDateSelected = { date ->
                            showTimePicker(context, date) { dateTime ->
                                onReminderDateChange(dateTime.time)
                                reminderText =
                                    DateFormat.format("HH:mm, dd MMM", dateTime).toString()
                            }
                        },
                        onClearReminder = {
                            onReminderDateChange(null)
                            reminderText = "Reminder"
                        }
                    )
                }
            }
        }
    )

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {},
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    TaskInputFields(
                        taskTitle,
                        taskDescription,
                        onTaskTitleChange,
                        onTaskDescriptionChange
                    )

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CategoryButton(
                            taskCategory = taskCategory,
                            expanded = expanded,
                            onExpandedChange = { expanded = it }
                        )

                        CategoryDropdown(
                            categoryList = categoryList,
                            onTaskCategoryChange = onTaskCategoryChange,
                            onDeleteCategory = onDeleteCategory,
                            onAddCategoryClick = onAddCategoryClick,
                            expanded = expanded,
                            onExpandedChange = { expanded = it }
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        ReminderButton(
                            reminderText = reminderText,
                            permissionLauncher = permissionLauncher
                        )

                    }

                }
            },
            confirmButton = {
                Button(
                    onClick = { onSaveTask(); onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryBackground)
                ) {
                    Text("Save", color = colors.thirdText)
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryBackground)
                ) {
                    Text("Cancel", color = colors.thirdText)
                }
            },
            containerColor = colors.SecondaryBackground
        )
    }
}










