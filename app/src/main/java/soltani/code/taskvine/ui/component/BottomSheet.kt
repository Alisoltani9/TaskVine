package soltani.code.taskvine.ui.component

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.viewmodel.CategoryViewModel
import soltani.code.taskvine.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    taskViewModel: TaskViewModel,
    categoryViewModel: CategoryViewModel,
    onDismiss: () -> Unit,
    onTaskAdded: () -> Unit,
    sharedPreferences: android.content.SharedPreferences // Pass SharedPreferences directly

) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val colors = LocalCustomColors.current
    val context = LocalContext.current

    // Read the reminder state from SharedPreferences
    val isReminderEnabled = remember { mutableStateOf(sharedPreferences.getBoolean("isReminderEnabled", true)) }


    // States for task input
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskCategory by remember { mutableStateOf("") }
    val taskCompleted by remember { mutableStateOf(false) }
    var reminderDate by remember { mutableStateOf<Long?>(null) }

    // Observe category list from CategoryViewModel
    val categoryList by categoryViewModel.categoryList.observeAsState(emptyList())
    Log.d("BottomSheet", "Categories observed: $categoryList")

    // ModalBottomSheet Composable
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.SecondaryBackground,
        dragHandle = null
    ) {

        BottomContent(
            taskTitle = taskTitle,
            taskDescription = taskDescription,
            taskCategory = taskCategory,
            onTaskTitleChange = { taskTitle = it },
            onTaskDescriptionChange = { taskDescription = it },
            onTaskCategoryChange = { taskCategory = it },
            onReminderDateChange = { date ->
                reminderDate = date
            },  // Callback for reminder date change
            onAddTaskClick = {
                if (reminderDate != null && !isReminderEnabled.value) {
                    // Notify user only if a reminder is set and reminders are disabled
                    Toast.makeText(
                        context,
                        "Reminder is disabled. Please enable it to receive notifications.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (taskTitle.isNotEmpty()) {
                    // Add task to ViewModel
                    taskViewModel.addTask(
                        taskTitle,
                        taskDescription,
                        taskCategory,
                        reminderDate,
                        taskCompleted
                    )
                    onTaskAdded()
                }
            },
            onAddCategoryClick = { taskCategory ->
                if (taskCategory.isNotEmpty()) {
                    categoryViewModel.addCategory(taskCategory)
                }


            },
            onDeleteCategory = { category ->
                categoryViewModel.deleteCategory(category)
            },
            categoryList = categoryList.map { it.name } // Passing the category names
        )
    }
}
