package com.haf.artha.presentation.account.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.AccountRepository
import com.haf.artha.data.local.entity.AccountEntity
import com.haf.artha.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountScreenViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {
    private val _accountList  = MutableStateFlow<UiState<List<AccountEntity>>>(UiState.Loading)
    val accountList = _accountList.asStateFlow()
    private fun getListAccount(){
        viewModelScope.launch {
            accountRepository.getAllAccounts().collect {
                _accountList.value = UiState.Success(it)
            }
        }
    }
    init {
        getListAccount()
    }
}