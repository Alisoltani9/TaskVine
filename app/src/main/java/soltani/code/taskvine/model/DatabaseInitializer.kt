package soltani.code.taskvine.model


import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import soltani.code.taskvine.model.Achievement
import soltani.code.taskvine.model.AchievementDao
import soltani.code.taskvine.model.Category
import soltani.code.taskvine.model.CategoryDao
import javax.inject.Inject

class DatabaseInitializer @Inject constructor(
    private val categoryDao: CategoryDao,
    private val achievementDao: AchievementDao
) {
    suspend fun initializeDatabase() {
        withContext(Dispatchers.IO) {

            // Check if categories table is empty and insert default categories
            if (categoryDao.getAllCategoriesSync().isEmpty()) {
                Log.d("DatabaseInitializer", "Inserting default categories")
                categoryDao.insertAll(defaultCategories())
            }

            // Insert default achievements if not already present
            val achievementsCount = achievementDao.getAchievementsCount()
            if (achievementsCount == 0) {
                Log.d("DatabaseInitializer", "Inserting default achievements")
                achievementDao.insertAchievement(defaultAchievements())
                Log.d("DatabaseInitializer", "Default achievements inserted successfully")
            } else {
                Log.d("DatabaseInitializer", "Achievements already exist in the database")
            }
        }
    }

    private fun defaultCategories(): List<Category> {
        return listOf(
            Category(name = "No Category"),
            Category(name = "Personal"),
            Category(name = "Work"),
            Category(name = "Study"),
        )
    }

    private fun defaultAchievements(): List<Achievement> {
        return listOf(
            Achievement(title = "Task Master - Complete 100 tasks", isAchieved = false),
            Achievement(title = "Daily Streak - Plan a Task 7 days in a row", isAchieved = false),
            Achievement(title = "Pro Planner - Plan 30 tasks in advance", isAchieved = false),
            Achievement(
                title = "Quick Finisher - Complete 10 tasks within 10 minutes",
                isAchieved = false
            ),
            Achievement(title = "Perfect Week - Complete all tasks for a week", isAchieved = false)
        )
    }
}
