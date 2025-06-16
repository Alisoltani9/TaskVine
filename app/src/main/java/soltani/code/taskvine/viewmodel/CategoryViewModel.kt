package soltani.code.taskvine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import soltani.code.taskvine.model.Category
import soltani.code.taskvine.model.CategoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {

    // LiveData to observe the list of categories
    val categoryList: LiveData<List<Category>> = categoryDao.getAllCategories()

    // Function to add a new category
    fun addCategory(name: String) {
        val newCategory = Category(name = name)

        viewModelScope.launch {
            categoryDao.upsertCategory(newCategory)
        }
    }

    // Function to delete a category by name
    fun deleteCategory(categoryName: String) {
        viewModelScope.launch {
            categoryDao.deleteCategory(categoryName)
        }
    }
}
