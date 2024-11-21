package com.haf.artha.data.local

import com.haf.artha.data.local.db.dao.CategoryDao
import com.haf.artha.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    suspend fun insertCategory(categories: List<CategoryEntity>) {
        categoryDao.insert(categories)
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

    suspend fun getCategoryByName(name: String): CategoryEntity? {
        return categoryDao.getCategoryByName(name)
    }

    suspend fun getCategoryById(categoryId: Int): CategoryEntity? {
        return categoryDao.getCategoryById(categoryId)
    }
}