package com.haf.artha.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.haf.artha.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun multiInsert(accounts: List<AccountEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun singleInsert(account: AccountEntity)

    @Update
    suspend fun update(account: AccountEntity)

    @Delete
    suspend fun delete(account: AccountEntity)

    @Query("SELECT * FROM account WHERE id = :accountId")
    suspend fun getAccountById(accountId: Int): AccountEntity?

    @Query("SELECT * FROM account")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT SUM(account_balance) FROM account")
    fun getTotalBalance(): Flow<Double>
}
