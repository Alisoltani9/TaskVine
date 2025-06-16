package soltani.code.taskvine.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

import soltani.code.taskvine.model.Category

@Dao
interface CategoryDao {

    @Upsert
    suspend fun upsertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<Category>)

    @Query("DELETE FROM Category WHERE name = :categoryName")
    suspend fun deleteCategory(categoryName: String)

    @Query("SELECT * FROM Category")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM Category")
    suspend fun getAllCategoriesSync(): List<Category>
}
