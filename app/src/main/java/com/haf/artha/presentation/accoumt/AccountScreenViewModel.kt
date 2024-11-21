package com.haf.artha.presentation.accoumt

import androidx.lifecycle.ViewModel
import com.haf.artha.data.local.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountScreenViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {
    fun getAllAccounts() = accountRepository.getAllAccounts()
}