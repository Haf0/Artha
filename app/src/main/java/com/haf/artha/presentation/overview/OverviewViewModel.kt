package com.haf.artha.presentation.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.TransactionRepository
import com.haf.artha.data.local.model.CategoryAmount
import com.haf.artha.data.local.model.TypeAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
): ViewModel() {
    private val _categoryData = MutableStateFlow<List<CategoryAmount>>(emptyList())
    val categoryData = _categoryData.asStateFlow()

    private val _transactionTypeData = MutableStateFlow<List<TypeAmount>>(emptyList())
    val transactionTypeData = _transactionTypeData.asStateFlow()

    fun getCategoriesAmount(year: Int, month: Int) {
        viewModelScope.launch {
            transactionRepository.getCategoryAmount(year,month).collect {
                _categoryData.value = it
            }
        }

    }

    fun getTransactionTypeAmount(year: Int, month: Int) {
        viewModelScope.launch {
            transactionRepository.getTransactionTypeAmount(year,month).collect {
                _transactionTypeData.value = it
            }
        }
    }

}