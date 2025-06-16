package soltani.code.taskvine.viewmodel

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import soltani.code.taskvine.model.Task
import soltani.code.taskvine.model.TaskDao
import soltani.code.taskvine.notification.AlarmHelper
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao, private val context: Context
)
{
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    val allTasks = taskDao.getTask()
    val completedTaskCount = taskDao.getCompletedTaskCount()
    val pendingTaskCount = taskDao.getPendingTaskCount(System.currentTimeMillis())
    val overdueTaskCount = taskDao.getOverdueTaskCount(System.currentTimeMillis())


    suspend fun addTask(task: Task): Long {
        val taskId = withContext(Dispatchers.IO) {
            val id = taskDao.upsertTask(task)
            task.reminderTime?.let {
                AlarmHelper.scheduleReminder(context, task.copy(id = id.toInt()))
            }
            id
        }

        val user = auth.currentUser
        if (user == null) {
            android.util.Log.e("TaskRepository", "No user signed in, task only saved locally.")
        } else {
            val taskWithId = task.copy(id = taskId.toInt())
            firestore.collection("users")
                .document(user.uid)
                .collection("tasks")
                .document(taskId.toString())
                .set(taskWithId)
                .addOnSuccessListener {
                    android.util.Log.d("TaskRepository", "Task uploaded to Firestore successfully.")
                }
                .addOnFailureListener { e ->
                    android.util.Log.e("TaskRepository", "Failed to upload task to Firestore", e)
                }
        }

        return taskId
    }


    suspend fun updateTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.upsertTask(task)
            if (task.reminderTime != null) {
                AlarmHelper.scheduleReminder(context, task)
            } else {
                AlarmHelper.cancelReminder(context, task.id)
            }
        }

        auth.currentUser?.let { user ->
            firestore.collection("users")
                .document(user.uid)
                .collection("tasks")
                .document(task.id.toString())
                .set(task)
        }
    }

    suspend fun deleteTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(task)
            AlarmHelper.cancelReminder(context, task.id)
        }

        auth.currentUser?.let { user ->
            firestore.collection("users")
                .document(user.uid)
                .collection("tasks")
                .document(task.id.toString())
                .delete()
        }
    }

    suspend fun completeTask(task: Task) {
        withContext(Dispatchers.IO) {
            val updatedTask = task.copy(completedTask = true)
            taskDao.upsertTask(updatedTask)

        }
    }

    suspend fun getTasksForDate(startOfDay: Long, endOfDay: Long): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.getTasksForDate(startOfDay, endOfDay)
        }
    }

    suspend fun searchTasks(query: String): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.searchTasks(query)
        }
    }

    suspend fun syncTasksFromFirebase() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val tasksRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .collection("tasks")

        val snapshot = tasksRef.get().await()
        val firebaseTasks = snapshot.toObjects(Task::class.java)
        Log.d("TaskSync", "Fetched ${firebaseTasks.size} tasks from Firestore")

        withContext(Dispatchers.IO) {
            for (task in firebaseTasks) {
                taskDao.upsertTask(task) // Avoid duplicates by using upsert
            }
        }
    }

    suspend fun clearLocalTasks() {
        return withContext(Dispatchers.IO) {
            taskDao.clearAll()
        }
    }

}
