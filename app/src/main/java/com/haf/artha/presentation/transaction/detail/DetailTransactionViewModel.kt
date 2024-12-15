package com.haf.artha.presentation.transaction.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.TransactionRepository
import com.haf.artha.data.model.TransactionDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
): ViewModel() {

    suspend fun getTransaction(id: Int) = transactionRepository.getTransactionById(id)
    fun deleteTransaction(transactionDetail: TransactionDetail) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transactionDetail)

        }

    }
}