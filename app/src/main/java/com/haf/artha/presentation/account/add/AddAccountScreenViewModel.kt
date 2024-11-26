package com.haf.artha.presentation.account.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.AccountRepository
import com.haf.artha.data.local.entity.AccountEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAccountScreenViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {
    var account = MutableStateFlow<AccountEntity>(AccountEntity(0, "", "", 0.0))
    fun addAccount(
        name: String,
        balance: Double
    ) {
        viewModelScope.launch {
            accountRepository.singleInsertAccount(name, balance)
        }
    }


    suspend fun getAccount(id: Int): AccountEntity?{
        return accountRepository.getAccountById(id)
    }

    fun updateAccount(accountId: Int, accountName: String, balance: Double) {
        viewModelScope.launch {
            val account = AccountEntity(accountId, accountName, accountName, balance)
            accountRepository.updateAccount(account)
        }
    }
}