package soltani.code.taskvine.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.ui.component.SimpleSearchBar
import soltani.code.taskvine.ui.component.TaskItem
import soltani.code.taskvine.ui.component.TaskItemLight
import soltani.code.taskvine.viewmodel.CategoryViewModel
import soltani.code.taskvine.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow

@Composable
fun SearchScreen(
    taskViewModel: TaskViewModel,
    categoryViewModel: CategoryViewModel
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val selectedMonth = YearMonth.from(selectedDate)
    val tasksForSelectedDate by taskViewModel.tasksForSelectedDate.observeAsState(emptyList())
    val taskCountByDate by taskViewModel.taskCountByDate.observeAsState(emptyMap())
    var query by remember { mutableStateOf("") }
    val searchResults by taskViewModel.searchResults.observeAsState(emptyList())
    var isCalendarVisible by remember { mutableStateOf(true) }
    val colors = LocalCustomColors.current

    LaunchedEffect(selectedDate) {
        taskViewModel.getTasksForSelectedDate(selectedDate)
        taskViewModel.fetchTaskCountsForMonth(selectedMonth)
    }

    Column(modifier = Modifier.padding(16.dp)) {

        SimpleSearchBar(
            query = query,
            onValueChange = { newQuery ->
                query = newQuery
                isCalendarVisible = newQuery.isEmpty()
                taskViewModel.searchTasks(query)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Animated visibility for the calendar
        AnimatedVisibility(
            visible = isCalendarVisible,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            MaterialCalendar(
                selectedDate = selectedDate,
                onDateSelected = { newDate ->
                    selectedDate = newDate
                    taskViewModel.getTasksForSelectedDate(newDate)
                },
                taskCountByDate = taskCountByDate
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (isCalendarVisible) {
            Text(
                text = "${selectedDate.formatMonthDay()} Tasks:",
                color = colors.primaryText,
                style = MaterialTheme.typography.titleMedium
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Allows it to take up remaining space
                .padding(8.dp)
        ) {
            val tasksToDisplay = if (query.isNotEmpty()) searchResults else tasksForSelectedDate

            if (tasksToDisplay.isEmpty()) {
                item {
                    Text(
                        text = if (query.isNotEmpty()) "No tasks found" else "No tasks for this day.",
                        color = colors.primaryText,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            } else {
                items(tasksToDisplay) { task ->
                    TaskItemLight(
                        task,
                        onCompleted = { taskViewModel.completeTask(task) },
                        onTaskDeleted = { taskViewModel.deleteTask(task) },
                        onTaskUpdated = { updatedTask -> taskViewModel.updateTask(updatedTask) },
                        categoryViewModel = categoryViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MaterialCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    taskCountByDate: Map<LocalDate, Int>
) {
    val pagerState = rememberPagerState(pageCount = { Int.MAX_VALUE })
    val coroutineScope = rememberCoroutineScope()
    val colors = LocalCustomColors.current

    LaunchedEffect(Unit) {
        coroutineScope.launch { pagerState.scrollToPage(5000) }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp) // Increase height to fit all rows
    ) { page ->
        val currentYearMonth = YearMonth.now().plusMonths(page - 5000L)
        val daysInMonth = generateDaysInMonthWithPlaceholders(currentYearMonth)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Month and Year Header
            Text(
                text = "${currentYearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentYearMonth.year}",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = colors.primaryText,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Day Names Row (e.g., Mon, Tue, Wed)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                DayOfWeek.entries.toTypedArray()
                    .run { slice(1..6) + slice(0..0) } // Start with Monday
                    .forEach { day ->
                        Text(
                            text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.primaryText,
                            fontSize = 10.sp, // Smaller font for day names
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(40.dp) // Set fixed width for each day name
                                .wrapContentHeight() // Only take necessary height
                        )
                    }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Days Grid (Dates)
            Column {
                daysInMonth.chunked(7).forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center // Center all items
                    ) {
                        week.forEach { day ->
                            val isSelected = day == selectedDate
                            val taskCount = taskCountByDate[day] ?: 0

                            Box(
                                modifier = Modifier
                                    .size(40.dp) // Set fixed size for each day cell
                                    .padding(2.dp)
                                    .clickable(enabled = day != null) {
                                        day?.let { onDateSelected(it) }
                                    }
                                    .background(
                                        color = if (isSelected) colors.SecondaryBackground else Color.Transparent,
                                        shape = MaterialTheme.shapes.small
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (taskCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(2.dp)
                                            .size(12.dp)
                                            .background(colors.primaryText, shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = taskCount.toString(),
                                            fontSize = 8.sp,
                                            color = colors.cardBackground,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                                Text(
                                    text = day?.dayOfMonth?.toString() ?: "",
                                    fontSize = 12.sp,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.primaryText,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun generateDaysInMonthWithPlaceholders(yearMonth: YearMonth): List<LocalDate?> {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()
    val emptyDaysAtStart = (firstDayOfMonth.dayOfWeek.value + 5) % 7

    val daysInMonth = (1..lastDayOfMonth.dayOfMonth).map { yearMonth.atDay(it) }
    val totalDays = emptyDaysAtStart + daysInMonth.size
    val emptyDaysAtEnd = if (totalDays % 7 == 0) 0 else 7 - (totalDays % 7)

    return List(emptyDaysAtStart) { null } + daysInMonth + List(emptyDaysAtEnd) { null }
}

fun LocalDate.formatMonthDay(): String =
    "${month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} $dayOfMonth"
