package soltani.code.taskvine.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.model.TabItems
import soltani.code.taskvine.ui.screens.dialogs.AchievementDialog
import soltani.code.taskvine.ui.component.BottomSheet
import soltani.code.taskvine.ui.component.TabRowSection
import soltani.code.taskvine.ui.theme.DarkCustomColors
import soltani.code.taskvine.ui.theme.GreenCustomColors
import soltani.code.taskvine.ui.theme.LightCustomColors
import soltani.code.taskvine.viewmodel.AchievementProvider
import soltani.code.taskvine.viewmodel.CategoryViewModel
import soltani.code.taskvine.viewmodel.TaskViewModel

@Composable
fun HomeScreen(
    taskViewModel: TaskViewModel,
    categoryViewModel: CategoryViewModel,
    achievementProvider: AchievementProvider
) {
    val tabItems = listOf(
        TabItems(title = "Today"),
        TabItems(title = "Upcoming"),
        TabItems(title = "Completed")
    )

    val colors = LocalCustomColors.current
    val context = LocalContext.current

    val taskList by taskViewModel.taskList.observeAsState(emptyList())
    var showSheet by remember { mutableStateOf(false) }

    val sharedPreferences = context.getSharedPreferences("reminderSettings", Context.MODE_PRIVATE)


    // Observe dialog title
    val dialogTitle by achievementProvider.dialogTitle.observeAsState(null)

    // Achievement Dialog
    if (dialogTitle != null) {
        AchievementDialog(
            dialogTitle = dialogTitle,
            onDismiss = { achievementProvider.resetDialog() }
        )
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .padding(4.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            TabRowSection(
                tabItems = tabItems,
                taskList = taskList,
                viewModel = taskViewModel,
                categoryViewModel = categoryViewModel
            )
        }

        FloatingActionButton(
            onClick = { showSheet = true },
            containerColor = when (colors) {
                GreenCustomColors -> colors.highlight
                LightCustomColors -> colors.secondaryText
                DarkCustomColors -> colors.cardBackground
                else -> MaterialTheme.colorScheme.primary
            },            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 10.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Task",   tint = when (colors) {
                GreenCustomColors -> colors.thirdText
                LightCustomColors -> colors.primaryBackground
                DarkCustomColors -> colors.primaryText
                else -> MaterialTheme.colorScheme.onPrimary

            })
        }

        if (showSheet) {
            BottomSheet(
                onDismiss = { showSheet = false },
                taskViewModel = taskViewModel,
                categoryViewModel = categoryViewModel,
                onTaskAdded = { showSheet = false },
                sharedPreferences = sharedPreferences // Pass SharedPreferences here

            )
        }
    }
}
