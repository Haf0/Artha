package com.haf.artha.presentation.overview

import android.util.Log
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
                Log.d("OVERVIEW", "getCategoriesAmount: $year $month $it" )
            }
        }

    }
// TODO add the previous month data fetch function
    private val _incomeData = MutableStateFlow<Double>(0.0)
    val incomeData = _incomeData.asStateFlow()
    fun getIncomeAmount(year: Int, month: Int) {
        viewModelScope.launch {
            transactionRepository.getTotalIncomeByMonth(year,month).collect {
                _incomeData.value = it
            }

        }
    }

    private val _expenseData = MutableStateFlow<Double>(0.0)
    val expenseData = _expenseData.asStateFlow()
    fun getExpenseAmount(year: Int, month: Int) {
        viewModelScope.launch {
            transactionRepository.getTotalExpenseByMonth(year,month).collect {
                _expenseData.value = it
            }
        }
    }


}