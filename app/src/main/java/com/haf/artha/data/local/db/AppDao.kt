package com.haf.artha.data.local.db

import androidx.room.Dao
import androidx.room.Query
import com.haf.artha.data.local.entity.AccountEntity
import com.haf.artha.data.local.entity.CategoryEntity
import com.haf.artha.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Query("SELECT * FROM account")
    fun getAccounts(): Flow<List<AccountEntity>>

    @Query("Select * FROM category")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM `transaction`")
    fun getTransactions(): Flow<List<TransactionEntity>>
}