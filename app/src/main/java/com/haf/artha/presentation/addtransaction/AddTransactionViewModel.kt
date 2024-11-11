package com.haf.artha.presentation.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.TransactionRepository
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.data.local.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
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
            transactionRepository.insertTransaction(
                TransactionEntity(
                    0,
                    accountId,
                    categoryId,
                    name,
                    date,
                    type,
                    note,
                    amount
                )
            )
        }
    }
}