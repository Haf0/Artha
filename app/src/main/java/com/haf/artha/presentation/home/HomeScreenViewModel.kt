package com.haf.artha.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.AccountRepository
import com.haf.artha.data.local.TransactionRepository
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.data.local.model.CombinedHomeState
import com.haf.artha.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
): ViewModel() {
    private val _totalBalance  = MutableStateFlow<UiState<Double>>(UiState.Loading)
    private val _totalIncome  = MutableStateFlow<UiState<Double>>(UiState.Loading)
    private val _totalExpense  = MutableStateFlow<UiState<Double>>(UiState.Loading)
    private val _recentTransaction  = MutableStateFlow<UiState<List<TransactionEntity>>>(UiState.Loading)

    val uiState: StateFlow<UiState<CombinedHomeState>> = combine(_totalBalance, _totalIncome, _totalExpense, _recentTransaction){
        totalBalance, totalIncome, totalExpense, recentTransaction ->
        if(totalBalance is UiState.Error || totalIncome is UiState.Error || totalExpense is UiState.Error || recentTransaction is UiState.Error){
            UiState.Error("Error")
        }else if(totalBalance is UiState.Loading || totalIncome is UiState.Loading || totalExpense is UiState.Loading || recentTransaction is UiState.Loading){
            UiState.Loading
        }else{
            UiState.Success(
                CombinedHomeState(
                    (totalBalance as UiState.Success).data,
                    (totalIncome as UiState.Success).data,
                    (totalExpense as UiState.Success).data,
                    (recentTransaction as UiState.Success).data
            )
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)

    fun getTotalBalance(){
        viewModelScope.launch {
            accountRepository.getTotalBalance().collect {
                _totalBalance.value = UiState.Success(it)
            }
        }
    }

    private fun getTotalIncome(){
        viewModelScope.launch {
            transactionRepository.getTotalIncomeThisMonth().collect {
                _totalIncome.value = UiState.Success(it)
            }
        }
    }
    private fun getTotalExpense(){
        viewModelScope.launch {
            transactionRepository.getTotalExpenseThisMonth().collect {
                _totalExpense.value = UiState.Success(it)
            }
        }
    }

    private fun getRecentTransactions(){
        viewModelScope.launch {
            transactionRepository.getRecentTransactions().collect {
                _recentTransaction.value = UiState.Success(it)
            }
        }
    }
    init {
        getTotalBalance()
        getTotalIncome()
        getTotalExpense()
        getRecentTransactions()
    }
}