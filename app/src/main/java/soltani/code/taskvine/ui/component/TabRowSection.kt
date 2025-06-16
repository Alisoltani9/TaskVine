// TabRowSection.kt
package soltani.code.taskvine.ui.component

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.helpers.isReminderInPast
import soltani.code.taskvine.helpers.isReminderToday
import soltani.code.taskvine.helpers.isReminderUpcoming
import soltani.code.taskvine.model.TabItems
import soltani.code.taskvine.model.Task
import soltani.code.taskvine.viewmodel.CategoryViewModel
import soltani.code.taskvine.viewmodel.TaskViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@Composable
fun TabRowSection(
    tabItems: List<TabItems>,
    taskList: List<Task>,
    viewModel: TaskViewModel,
    categoryViewModel: CategoryViewModel
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabItems.size }

    val colors = LocalCustomColors.current
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("reminderSettings", Context.MODE_PRIVATE)


    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 40.dp, start = 4.dp, end = 4.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.primaryBackground),
            containerColor = colors.primaryBackground,
            contentColor = colors.primaryText,
            indicator = { /* No indicator here */ },
            divider = {}
        ) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = if (index == selectedTabIndex) 17.sp else 14.sp,
                                fontWeight = if (index == selectedTabIndex) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = colors.primaryText // Use theme's primary color
                        )
                    }
                )
            }
        }

        // Horizontal Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { pageIndex ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
            ) {
                val filteredTasks = when (pageIndex) {
                    0 -> taskList.filter { task ->
                        !task.completedTask && (
                                task.reminderTime == null ||
                                        isReminderToday(task.reminderTime) ||
                                        isReminderInPast(task.reminderTime)
                                )
                    }

                    1 -> taskList.filter { task ->
                        !task.completedTask && (
                                task.reminderTime?.let { isReminderUpcoming(it) } == true
                                )
                    }

                    2 -> taskList.filter { task ->
                        task.completedTask
                    }

                    else -> emptyList()
                }

                // Default text based on selected tab
                val defaultText = when (pageIndex) {
                    0 -> "No tasks for today! Let's get started."
                    1 -> "No upcoming tasks. Plan ahead!"
                    2 -> "No completed tasks yet."
                    else -> "Let's plan something!"
                }

                if (filteredTasks.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        items(filteredTasks) { task ->
                            TaskItem(
                                task,
                                onCompleted = { viewModel.completeTask(task) },
                                onTaskDeleted = { viewModel.deleteTask(task) },
                                onTaskUpdated = { updatedTask -> viewModel.updateTask(updatedTask) },
                                categoryViewModel = categoryViewModel,
                                sharedPreferences = sharedPreferences // Pass SharedPreferences here

                            )
                        }
                    }
                } else {
                    Text(
                        text = defaultText,
                        fontSize = 18.sp,
                        color = colors.thirdText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(alignment = Alignment.Center)
                    )
                }
            }
        }
    }
}
