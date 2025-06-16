package soltani.code.taskvine.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soltani.code.taskvine.helpers.Converters

@Database(entities = [Category::class, Achievement::class, Task::class], version = 2,exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun achievementDao(): AchievementDao
    abstract fun getTaskDao(): TaskDao

    companion object {
        private const val DATABASE_NAME = "Task_DB"

        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
