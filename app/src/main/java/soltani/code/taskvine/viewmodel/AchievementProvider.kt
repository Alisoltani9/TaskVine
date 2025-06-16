package soltani.code.taskvine.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import soltani.code.taskvine.helpers.isReminderUpcoming
import soltani.code.taskvine.model.Achievement
import soltani.code.taskvine.model.AchievementDao
import soltani.code.taskvine.model.Task
import soltani.code.taskvine.model.TaskDao
import soltani.code.taskvine.ui.component.toLocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class AchievementProvider @Inject constructor(
    private val achievementDao: AchievementDao,
    private val taskDao: TaskDao,

) {

    val achievementList: LiveData<List<Achievement>> = achievementDao.getAllAchievements()

    // Dialog state
    private val _dialogTitle = MutableLiveData<String?>()
    val dialogTitle: LiveData<String?> get() = _dialogTitle

    // Method to trigger dialog display
    fun showAchievementDialog(title: String) {
        _dialogTitle.postValue(title)
    }

    // Method to reset dialog state
    fun resetDialog() {
        _dialogTitle.postValue(null)
    }

    suspend fun checkAndUpdateTaskMasterAchievement() {
        val TAG = "TaskMasterAchievement"

        // Fetch the total number of completed tasks
        val completedTasksCount = withContext(Dispatchers.IO) {
            taskDao.getCompletedTasksCount()
        }

        Log.d(TAG, "Total completed tasks: $completedTasksCount")

        // Check if the achievement should be updated
        val isAchieved = completedTasksCount >= 100
        val achievement = achievementDao.getAchievementByTitle("Task Master - Complete 100 tasks")

        if (achievement != null && achievement.isAchieved != isAchieved) {
            achievementDao.updateAchievementStatus(achievement.id, isAchieved)
            Log.i(TAG, "Achievement updated: ${achievement.title}, Status: $isAchieved")
        } else if (achievement == null) {
            Log.w(TAG, "Achievement not found.")
        } else {
            Log.d(TAG, "Achievement status already correct: ${achievement.isAchieved}")
        }
    }


    suspend fun checkAndUpdateDailyStreak() {
        val sevenDaysAgo = LocalDate.now()
            .minusDays(7)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val plannedTasks = taskDao.getTasksPlannedInLast7Days(sevenDaysAgo)

        val streakDays = calculateStreak(plannedTasks)

        val isAchieved = streakDays == 7

        val achievement = achievementDao.getAchievementByTitle("Daily Streak - Plan a Task 7 days in a row")
        if (achievement != null && achievement.isAchieved != isAchieved) {
            achievementDao.updateAchievementStatus(achievement.id, isAchieved)
        }
    }

    private fun calculateStreak(tasks: List<Task>): Int {
        val currentDate = LocalDate.now()
        var streakCount = 0
        var streakDate = currentDate

        for (i in 0 until 7) {
            if (tasks.any { it.created_at.toLocalDate() == streakDate }) {
                streakCount++
            } else {
                break
            }
            streakDate = streakDate.minusDays(1)
        }

        return streakCount
    }

    suspend fun checkAndUpdateProPlanner() {
        // Get all tasks with a reminder
        val allTasks = taskDao.getAllTasks()

        // Filter tasks that are only scheduled for the day after today
        val upcomingTasks = allTasks.filter { task ->
            task.reminderTime != null && isReminderUpcoming(task.reminderTime)
        }

        // Check if at least 30 tasks are planned for upcoming days
        val isAchieved = upcomingTasks.size >= 30

        // Update the achievement status
        val achievement = achievementDao.getAchievementByTitle("Pro Planner - Plan 30 tasks in advance")
        if (achievement != null && achievement.isAchieved != isAchieved) {
            achievementDao.updateAchievementStatus(achievement.id, isAchieved)
        }
    }

    suspend fun checkAndUpdateQuickFinisherAchievement() {
        // Fetch all completed tasks
        val completedTasks = taskDao.getAllCompletedTasks()

        // Define a time threshold of 10 minutes in milliseconds
        val tenMinutesInMillis = 10 * 60 * 1000

        // Check if any task was completed within 10 minutes of creation
        val quickFinishers = completedTasks.filter { task ->
            val createdTime = task.created_at
            val completedTime = System.currentTimeMillis()
            completedTime - createdTime <= tenMinutesInMillis
        }

        // Count the number of tasks completed within 10 minutes
        val quickFinishCount = quickFinishers.size

        Log.d("QuickFinish", quickFinishCount.toString())

        // Update the achievement status
        val achievement = achievementDao.getAchievementByTitle("Quick Finisher - Complete 10 tasks within 10 minutes")
        if (quickFinishCount >= 2 && achievement != null && !achievement.isAchieved) {
            achievementDao.updateAchievementStatus(achievement.id, true)


        }
    }

    suspend fun checkAndUpdatePerfectWeekAchievement() {
        val TAG = "PerfectWeekAchievement"
        val sevenDaysAgo = LocalDate.now().minusDays(7)
        val today = LocalDate.now()

        // Fetch all tasks within the last 7 days
        val tasks = withContext(Dispatchers.IO) {
            taskDao.getTasksBetweenDates(
                (sevenDaysAgo.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000).toString(),
                (today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000).toString()
            )
        }

        Log.d(TAG, "Fetched ${tasks.size} tasks in the last 7 days.")
        tasks.forEach {
            Log.d(TAG, "Task ID: ${it.id}, Title: ${it.titleTask}, Completed: ${it.completedTask}, Date: ${it.created_at.toLocalDate()}")
        }

        // Check for each day if there's a completed task
        var streakExists = true
        for (day in 0..6) {
            val date = sevenDaysAgo.plusDays(day.toLong())
            val tasksForDay = tasks.filter { it.created_at.toLocalDate() == date && it.completedTask }

            if (tasksForDay.isEmpty()) {
                Log.w(TAG, "No completed task on: $date")
                streakExists = false
            } else {
                Log.d(TAG, "Completed tasks on $date: ${tasksForDay.size}")
            }
        }

        // Update the achievement status based on the streak check
        val isAchieved = streakExists
        val achievement = achievementDao.getAchievementByTitle("Perfect Week - Complete all tasks for a week")
        if (achievement != null && achievement.isAchieved != isAchieved) {
            achievementDao.updateAchievementStatus(achievement.id, isAchieved)
            Log.i(TAG, "Achievement updated: ${achievement.title}, Status: $isAchieved")
        } else if (achievement == null) {
            Log.w(TAG, "Achievement not found.")
        } else {
            Log.d(TAG, "Achievement status already correct: ${achievement.isAchieved}")
        }
    }




    suspend fun checkAchievementsAndTriggerDialog() {
        val achievements = withContext(Dispatchers.IO) {
            achievementDao.getAllAchievementsSuspend()
        }

        // Find and show the first achievement that is newly achieved
        achievements.find { it.isAchieved }?.let {
            showAchievementDialog(it.title)
        }
    }
    suspend fun checkAndUpdateAllAchievements() {
        checkAndUpdateTaskMasterAchievement()
        checkAndUpdateDailyStreak()
        checkAndUpdateProPlanner()
        checkAndUpdateQuickFinisherAchievement()
        checkAndUpdatePerfectWeekAchievement()
        checkAchievementsAndTriggerDialog()

    }


}
