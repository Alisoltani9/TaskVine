package soltani.code.taskvine.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import soltani.code.taskvine.model.Achievement

@Dao
interface AchievementDao {

    @Query("SELECT * FROM Achievement")
    fun getAllAchievements(): LiveData<List<Achievement>>

    @Query("SELECT * FROM Achievement")
    suspend fun getAllAchievementsSuspend(): List<Achievement> // Suspend version for coroutine usage

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievements: List<Achievement>)

    @Query("UPDATE Achievement SET isAchieved = :status WHERE id = :achievementId")
    suspend fun updateAchievementStatus(achievementId: Int, status: Boolean)

    @Query("SELECT * FROM Achievement WHERE title = :title LIMIT 1")
    suspend fun getAchievementByTitle(title: String): Achievement?


    @Query("SELECT COUNT(*) FROM Achievement")
    fun getAchievementsCount(): Int

}
