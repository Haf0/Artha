package com.haf.artha.data.local

import com.haf.artha.data.local.db.dao.AccountDao
import com.haf.artha.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountDao: AccountDao
) {
    suspend fun insertAccount(accounts: List<AccountEntity>) {
        accountDao.multiInsert(accounts)
    }

    suspend fun singleInsertAccount(name: String, balance: Double) {
        val account = AccountEntity(name = name, type = name, balance = balance)
        accountDao.singleInsert(account)
    }
    suspend fun updateAccount(account: AccountEntity) {
        accountDao.update(account)
    }

    suspend fun deleteAccount(accountId: Int) {
        accountDao.delete(accountId)
    }

    fun getAllAccounts(): Flow<List<AccountEntity>> {
        return accountDao.getAllAccounts()
    }

    //sum of all account balance
    fun getTotalBalance(): Flow<Double> {
        return accountDao.getTotalBalance()
    }

    suspend fun getAccountById(accountId: Int): AccountEntity? {
        return accountDao.getAccountById(accountId)
    }
}