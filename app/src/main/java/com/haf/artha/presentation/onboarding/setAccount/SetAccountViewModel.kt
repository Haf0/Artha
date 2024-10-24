package com.haf.artha.presentation.onboarding.setAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haf.artha.data.local.AccountRepository
import com.haf.artha.data.local.entity.AccountEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetAccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {
    fun insertAccount(accounts: List<AccountEntity>) {
        viewModelScope.launch { accountRepository.insertAccount(accounts) }
    }
}