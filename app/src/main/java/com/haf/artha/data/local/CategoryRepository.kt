package com.haf.artha.data.local

import com.haf.artha.data.local.db.CategoryDao
import com.haf.artha.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    private val categoryDao: CategoryDao
) {
    suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insert(category)
    }

    suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.update(category)
    }

    suspend fun deleteCategory(category: CategoryEntity) {
        categoryDao.delete(category)
    }

    fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

    suspend fun getCategoryById(categoryId: Int): CategoryEntity? {
        return categoryDao.getCategoryById(categoryId)
    }
}