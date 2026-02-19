package com.haf.artha.presentation.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.TransactionRepository
import com.haf.artha.data.local.model.CategoryAmount
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

    fun getCategoriesAmount(year: Int, month: Int) {
        viewModelScope.launch {
            transactionRepository.getCategoryAmount(year,month).collect {
                _categoryData.value = it
            }
        }

    }
    private val _incomeData = MutableStateFlow<Double>(0.0)
    val incomeData = _incomeData.asStateFlow()

    private val _previousIncomeData = MutableStateFlow<Double>(0.0)
    val previousIncomeData = _previousIncomeData.asStateFlow()

    fun getIncomeAmount(year: Int, month: Int) {
        viewModelScope.launch {
            transactionRepository.getTotalIncomeByMonth(year,month).collect {
                _incomeData.value = it
            }
        }
    }

    fun getPreviousIncomeAmount(year: Int, month: Int) {
        viewModelScope.launch {
            val previousMonth = if (month == 1) 12 else month - 1
            val previousYear = if (month == 1) year - 1 else year
            transactionRepository.getTotalIncomeByMonth(previousYear,previousMonth).collect {
                _previousIncomeData.value = it
            }
        }
    }

    private val _expenseData = MutableStateFlow<Double>(0.0)
    val expenseData = _expenseData.asStateFlow()

    private val _previousExpenseData = MutableStateFlow<Double>(0.0)
    val previousExpenseData = _previousExpenseData.asStateFlow()

    fun getExpenseAmount(year: Int, month: Int) {
        viewModelScope.launch {
            transactionRepository.getTotalExpenseByMonth(year,month).collect {
                _expenseData.value = it
            }
        }
    }

    fun getPreviousExpenseAmount(year: Int, month: Int) {
        viewModelScope.launch {
            val previousMonth = if (month == 1) 12 else month - 1
            val previousYear = if (month == 1) year - 1 else year
            transactionRepository.getTotalExpenseByMonth(previousYear,previousMonth).collect {
                _previousExpenseData.value = it
            }
        }
    }


}