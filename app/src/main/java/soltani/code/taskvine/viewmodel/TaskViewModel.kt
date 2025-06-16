package soltani.code.taskvine.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import soltani.code.taskvine.model.Task
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    application: Application,
    private val taskRepository: TaskRepository
) : AndroidViewModel(application) {



    val taskList: LiveData<List<Task>> = taskRepository.allTasks
    val completedTaskCount: LiveData<Int> = taskRepository.completedTaskCount
    val pendingTaskCount: LiveData<Int> = taskRepository.pendingTaskCount
    val overdueTaskCount: LiveData<Int> = taskRepository.overdueTaskCount

    val snackbarMessage = MutableLiveData<String?>()
    var lastCompletedTask: Task? = null
    var lastDeletedTask: Task? = null

    private val _tasksForSelectedDate = MutableLiveData<List<Task>>()
    val tasksForSelectedDate: LiveData<List<Task>> = _tasksForSelectedDate

    private val _searchResults = MutableLiveData<List<Task>>(emptyList())
    val searchResults: LiveData<List<Task>> = _searchResults

    private val _taskCountByDate = MutableLiveData<Map<LocalDate, Int>>()
    val taskCountByDate: LiveData<Map<LocalDate, Int>> = _taskCountByDate

    fun addTask(title: String, description: String, category: String, reminderDate: Long?, completedTask: Boolean) {
        viewModelScope.launch {
            val task = Task(
                titleTask = title,
                descriptionTask = description,
                categoryTask = category,
                reminderTime = reminderDate,
                completedTask = completedTask,
                created_at = System.currentTimeMillis()
            )

            taskRepository.addTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            lastDeletedTask = task
            taskRepository.deleteTask(task)
        }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch {
            lastCompletedTask = task
            taskRepository.completeTask(task)
            snackbarMessage.postValue("Task '${task.titleTask}' Completed!")
        }
    }

    fun undoCompleteTask() {
        lastCompletedTask?.let { task ->
            viewModelScope.launch {
                taskRepository.updateTask(task.copy(completedTask = false))
                lastCompletedTask = null
            }
        }
    }

    fun clearSnackbarMessage() {
        snackbarMessage.value = null
    }

    fun getTasksForSelectedDate(date: LocalDate) {
        viewModelScope.launch {
            val startOfDay = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endOfDay = date.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val tasks = taskRepository.getTasksForDate(startOfDay, endOfDay)
            _tasksForSelectedDate.postValue(tasks)
        }
    }

    fun fetchTaskCountsForMonth(yearMonth: YearMonth) {
        viewModelScope.launch {
            val daysInMonth = yearMonth.lengthOfMonth()
            val taskCounts = mutableMapOf<LocalDate, Int>()

            for (day in 1..daysInMonth) {
                val date = yearMonth.atDay(day)
                val startOfDay = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                val endOfDay = date.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli()

                val tasksForDay = taskRepository.getTasksForDate(startOfDay, endOfDay)
                taskCounts[date] = tasksForDay.size
            }

            _taskCountByDate.postValue(taskCounts)
        }
    }

    fun searchTasks(query: String) {
        viewModelScope.launch {
            val results = taskRepository.searchTasks(query)
            _searchResults.postValue(results)
        }
    }
}
