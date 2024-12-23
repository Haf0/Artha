package com.haf.artha.presentation.transaction.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.AccountRepository
import com.haf.artha.data.local.CategoryRepository
import com.haf.artha.data.local.TransactionRepository
import com.haf.artha.data.local.entity.AccountEntity
import com.haf.artha.data.local.entity.CategoryEntity
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.data.local.model.TransactionType
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
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {
    fun addTransaction(
        type: TransactionType,
        name: String,
        accountId: Int,
        categoryId: Int,
        date: Long,
        note: String,
        amount: Double,
    ){
        viewModelScope.launch{
            Log.d("insertvm", "addTransaction: transactionType: $type, name: $name, accountId: $accountId, categoryId: $categoryId, date: $date, note: $note, amount: $amount")
            transactionRepository.insertTransaction(
                TransactionEntity(
                    0,
                    accountId,
                    categoryId,
                    name,
                    date,
                    type,
                    null,
                    note,
                    amount
                )
            )
        }
    }


    fun transferFunds(
        fromAccount: AccountEntity,
        toAccount:AccountEntity,
        amount: Double,
        date: Long
    ) {
        viewModelScope.launch {
            val transferCategory = categoryRepository.getCategoryByName("Transfer")
            val transferCategoryId = transferCategory?.id ?: 0
            Log.d(TAG, "transferFunds: ${fromAccount.id} ${toAccount.id} $amount $date $transferCategoryId")
            transactionRepository.insertTransaction(
                TransactionEntity(
                    0,
                    fromAccount.id,
                    transferCategoryId,
                    "Transfer ke ${toAccount.name}",
                    date,
                    TransactionType.TRANSFER,
                    toAccount.id,
                    "Transfer dari ${fromAccount.name} ke ${toAccount.name}",
                    amount
                )
            )
        }
    }

    private val _categories = MutableStateFlow<UiState<List<CategoryEntity>>>(UiState.Loading)
    private val _accounts = MutableStateFlow<UiState<List<AccountEntity>>>(UiState.Loading)

    val uiState: StateFlow<UiState<Pair<List<CategoryEntity>, List<AccountEntity>>>> = combine(_categories, _accounts) { categories, accounts ->
        if (categories is UiState.Success && accounts is UiState.Success) {
            UiState.Success(Pair(categories.data, accounts.data))
        } else if (categories is UiState.Error || accounts is UiState.Error) {
            UiState.Error("Error loading data")
        } else {
            UiState.Loading
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)


    init {
        fetchCategories()
        fetchAccounts()
    }


    private fun fetchCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect{
                _categories.value = UiState.Success(it)
            }
        }
    }
    private fun fetchAccounts() {
        viewModelScope.launch {
            accountRepository.getAllAccounts().collect{
                _accounts.value = UiState.Success(it)
                Log.d("getCategories", "categories: ${it}")
            }
        }
    }




    fun editTransaction(
        transaction: TransactionEntity,
        name: String,
        accountId: Int,
        categoryId: Int,
        date: Long,
        note: String,
        amount: Double
    ) {
        viewModelScope.launch {
            transactionRepository.updateTransaction(
                transaction.copy(
                    name =  name,
                    accountId = accountId,
                    categoryId = categoryId,
                    date = date,
                    note = note,
                    amount = amount
                )
            )
        }
    }

}