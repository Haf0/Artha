package com.haf.artha.data.local

import com.haf.artha.data.local.db.AppDao
import com.haf.artha.data.local.entity.AccountEntity
import com.haf.artha.data.local.entity.CategoryEntity
import com.haf.artha.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

class LocalRepository(
    private val appDao: AppDao
) {
    fun getAccounts() : Flow<List<AccountEntity>> = appDao.getAccounts()
    fun getCategories() : Flow<List<CategoryEntity>> = appDao.getCategories()
    fun getTransactions() : Flow<List<TransactionEntity>> = appDao.getTransactions()
    suspend fun insertCategory(listCategory: List<CategoryEntity>){
        listCategory.forEach {
            appDao.insertCategory(it)
        }
    }
    suspend fun insertAccount(listAccount: List<AccountEntity>){
        listAccount.forEach {
            appDao.insertAccount(it)
        }
    }
}