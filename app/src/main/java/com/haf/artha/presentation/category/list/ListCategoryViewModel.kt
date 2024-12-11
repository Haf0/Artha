package com.haf.artha.presentation.category.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.CategoryRepository
import com.haf.artha.data.local.entity.CategoryEntity
import com.haf.artha.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _categoryList  = MutableStateFlow<UiState<List<CategoryEntity>>>(UiState.Loading)
    val categoryList = _categoryList.asStateFlow()

    fun updateCategory(categories: CategoryEntity) {
        viewModelScope.launch {
            categoryRepository.updateCategory(categories)
        }
    }

    fun deleteCategoryById(categoryId: Int) {
        viewModelScope.launch {
            try {
                categoryRepository.deleteCategoryById(categoryId)
            } catch (e: Exception) {
                Log.d("CategoryScreen", "deleteCategoryById: $e")
            }
        }
    }
    private fun getAllCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect {
                _categoryList.value = UiState.Success(it)
            }
        }
    }

    fun insertCategory(categories: List<CategoryEntity>) {
        viewModelScope.launch {
            categoryRepository.insertCategory(categories)
        }
    }

    init {
        getAllCategories()
    }
}