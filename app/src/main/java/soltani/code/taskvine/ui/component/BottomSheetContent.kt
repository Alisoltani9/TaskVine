package soltani.code.taskvine.ui.component

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import soltani.code.taskvine.helpers.showDatePicker
import soltani.code.taskvine.helpers.showTimePicker
import soltani.code.taskvine.ui.screens.dialogs.AddCategoryDialog
import soltani.code.taskvine.ui.screens.dialogs.DeleteCategoryDialog
import soltani.code.taskvine.ui.component.bottomSheetContent.CategoryDropdownBs
import soltani.code.taskvine.ui.component.bottomSheetContent.FloatingAddButton
import soltani.code.taskvine.ui.component.bottomSheetContent.ReminderButtonBs
import soltani.code.taskvine.ui.component.bottomSheetContent.TaskInputFieldsBs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun BottomContent(
    modifier: Modifier = Modifier,
    taskTitle: String,
    taskDescription: String,
    taskCategory: String,
    onTaskTitleChange: (String) -> Unit,
    onTaskDescriptionChange: (String) -> Unit,
    onTaskCategoryChange: (String) -> Unit,
    onReminderDateChange: (Long?) -> Unit,
    onAddTaskClick: () -> Unit,
    onAddCategoryClick: (String) -> Unit,
    categoryList: List<String>,
    onDeleteCategory: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    var expanded by remember { mutableStateOf(false) }
    var reminderText by remember { mutableStateOf("Reminder") }
    var isDialogOpen by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<String?>(null) }
    var isDeleteCategoryDialogOpen by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                showDateTimePicker(
                    context,
                    coroutineScope,
                    onReminderDateChange,
                    onReminderTextChange = { reminderText = it } // Update reminderText here
                )
            } else {
                Log.d("BottomContent", "Permission Denied")
            }
        }
    )

    Column(modifier.padding(16.dp)) {
        TaskInputFieldsBs(
            taskTitle, taskDescription, onTaskTitleChange, onTaskDescriptionChange, focusRequester
        )

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceEvenly
        )
        {
            CategoryDropdownBs(
                expanded,
                taskCategory,
                categoryList,
                onTaskCategoryChange,
                onDialogOpen = { isDialogOpen = true },
                onDropdownExpand = { expanded = it },
                onLongPressCategory = { category ->
                    if (category != "No Category") {
                        categoryToDelete = category
                        isDeleteCategoryDialogOpen = true
                    }

                }
            )
            Spacer(modifier = Modifier.width(8.dp))

            ReminderButtonBs(
                reminderText = reminderText,
                context = context,
                coroutineScope = coroutineScope,
                permissionLauncher = permissionLauncher,
                onReminderDateChange = { onReminderDateChange(it) },
                onReminderTextChange = { newText ->
                    // Update button text here
                    reminderText = newText
                }
            )
            Spacer(modifier = Modifier.weight(1f))

            FloatingAddButton(onAddTaskClick)

        }



        if (isDialogOpen) {
            AddCategoryDialog(
                isDialogOpen = isDialogOpen,
                onDismiss = { isDialogOpen = false },
                onAddCategory = { newCategoryName ->
                    onAddCategoryClick(newCategoryName)
                    onTaskCategoryChange(newCategoryName)
                    isDialogOpen = false
                }
            )
        }

        if (isDeleteCategoryDialogOpen) {
            DeleteCategoryDialog(
                categoryToDelete, onDismiss = { isDeleteCategoryDialogOpen = false },
                onDeleteCategory = { category ->
                    onDeleteCategory(category)
                    isDeleteCategoryDialogOpen = false
                }
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


