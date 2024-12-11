package com.haf.artha.presentation.transaction.list

import androidx.lifecycle.ViewModel
import com.haf.artha.data.local.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
):ViewModel() {
    val transactions = transactionRepository.getAllTransactions()
}