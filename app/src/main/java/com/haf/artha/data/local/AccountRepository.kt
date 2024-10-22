package com.haf.artha.data.local

import com.haf.artha.data.local.db.AccountDao
import com.haf.artha.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

class AccountRepository(
    private val accountDao: AccountDao
) {
    suspend fun insertAccount(account: AccountEntity) {
        accountDao.insert(account)
    }

    suspend fun updateAccount(account: AccountEntity) {
        accountDao.update(account)
    }

    suspend fun deleteAccount(account: AccountEntity) {
        accountDao.delete(account)
    }

    fun getAllAccounts(): Flow<List<AccountEntity>> {
        return accountDao.getAllAccounts()
    }

    suspend fun getAccountById(accountId: Int): AccountEntity? {
        return accountDao.getAccountById(accountId)
    }
}