package com.haf.artha.presentation.onboarding.setCategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.CategoryRepository
import com.haf.artha.data.local.entity.CategoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {
    fun insertCategory(categories: List<CategoryEntity>) {
        viewModelScope.launch { categoryRepository.insertCategory(categories) }
    }
}