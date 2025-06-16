package soltani.code.taskvine.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDate

@Dao
interface TaskDao {


    @Query("SELECT * FROM Task")
    fun getTask(): LiveData<List<Task>>

    @Query("SELECT * FROM Task")
    suspend fun getAllTasks(): List<Task>

    @Query("SELECT * FROM task WHERE completedTask = 1")
    suspend fun getAllCompletedTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTask(task: Task): Long

    @Delete
    suspend fun deleteTask(task: Task)


    @Query("SELECT * FROM task WHERE reminderTime BETWEEN :startTime AND :endTime")
    fun getTasksForDate(startTime: Long, endTime: Long): List<Task>

    @Query("SELECT * FROM task WHERE reminderTime BETWEEN :startOfDay AND :endOfDay")
    suspend fun getTasksForToday(startOfDay: Long, endOfDay: Long): List<Task>


    @Query("SELECT * FROM task WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Query("SELECT * FROM task WHERE titleTask LIKE '%' || :query || '%'")
    fun searchTasks(query: String): List<Task>

    @Query("SELECT COUNT(*) FROM task WHERE completedTask = 1")
    fun getCompletedTaskCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM task WHERE completedTask = 1")
    suspend fun getCompletedTasksCount(): Int

    @Query("SELECT COUNT(*) FROM task WHERE completedTask = 0 AND (reminderTime IS NULL OR reminderTime > :currentTime)")
    fun getPendingTaskCount(currentTime: Long): LiveData<Int>

    @Query("SELECT * FROM task WHERE completedTask = 0 AND reminderTime IS NOT NULL AND reminderTime > :currentTime ORDER BY reminderTime ASC LIMIT 5")
    fun getPendingTask(currentTime: Long): List<Task>

    @Query("SELECT COUNT(*) FROM task WHERE completedTask = 0 AND reminderTime <= :currentTime")
    fun getOverdueTaskCount(currentTime: Long): LiveData<Int>


    @Query("SELECT * FROM Task WHERE created_at >= :sevenDaysAgo")
    suspend fun getTasksPlannedInLast7Days(sevenDaysAgo: Long): List<Task>

    // Add this method to calculate the date 7 days ago from today
    fun getSevenDaysAgo(): LocalDate {
        return LocalDate.now().minusDays(7)
    }

    @Query("SELECT * FROM Task WHERE completedTask = 1 AND created_at BETWEEN :start AND :end")
    suspend fun getTasksBetweenDates(start: String, end: String): List<Task>

    @Query("DELETE FROM task")
    suspend fun clearAll()
}