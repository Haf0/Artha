@file:OptIn(ExperimentalCoroutinesApi::class)

package com.haf.artha.presentation.transaction.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.TransactionRepository
import com.haf.artha.data.local.model.TransactionType
import com.haf.artha.data.model.TransactionFilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ListTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
):ViewModel() {
    val transactions = transactionRepository.getAllTransactions()

    private val _filterState = MutableStateFlow(TransactionFilterState())

    val filteredTransactions =
        _filterState.flatMapLatest { filterState->
            transactionRepository.filterTransactions(
                filterState
            ).onEach {
                Log.d("list", ": $it")
            }
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun updateSearchQuery(query: String) {
        _filterState.value = _filterState.value.copy(searchText = query)
    }
    fun updateAmountRange(minAmount: Double, maxAmount: Double) {
        _filterState.value = _filterState.value.copy(minAmount = minAmount, maxAmount = maxAmount)
    }

    fun updateDateRange(startDate: Long, endDate: Long) {
        _filterState.value = _filterState.value.copy(startDate = startDate, endDate = endDate)
    }

    fun updateTransactionTypes(transactionTypes: List<TransactionType>) {
        _filterState.value = _filterState.value.copy(transactionTypes = transactionTypes)
    }

    fun updateCategories(categories: List<Int>) {
        _filterState.value = _filterState.value.copy(categories = categories)
    }

    fun updateAccountId(accountId: Int) {
        _filterState.value = _filterState.value.copy(accountId = accountId)
    }

    fun clearFilters() {
        _filterState.value = TransactionFilterState()
    }

}