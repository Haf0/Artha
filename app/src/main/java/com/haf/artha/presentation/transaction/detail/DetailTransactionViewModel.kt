package com.haf.artha.presentation.transaction.detail

import androidx.lifecycle.ViewModel
import com.haf.artha.data.local.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class DetailTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
): ViewModel() {

    suspend fun getTransaction(id: Int) = transactionRepository.getTransactionById(id)
}