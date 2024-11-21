package com.haf.artha.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.data.local.model.TransactionType
import kotlinx.coroutines.flow.Flow


@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransactionById(transactionId: Int): TransactionEntity

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startOfDay AND :endOfDay")
    fun getTransactionsBySpecificDay(startOfDay: Long, endOfDay: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startOfWeek AND :endOfWeek")
    fun getTransactionsBySpecificWeek(startOfWeek: Long, endOfWeek: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startOfMonth AND :endOfMonth")
    fun getTransactionsBySpecificMonth(startOfMonth: Long, endOfMonth: Long): Flow<List<TransactionEntity>>

    //SUM this month Income transactions
    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND date BETWEEN :startOfMonth AND :endOfMonth")
    fun getTotalIncomeThisMonth(startOfMonth: Long, endOfMonth: Long, type:TransactionType): Flow<Double>

    //SUM this month Expense transactions
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Pengeluaran' AND date BETWEEN :startOfMonth AND :endOfMonth")
    fun getTotalExpenseThisMonth(startOfMonth: Long, endOfMonth: Long): Flow<Double>

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT 15")
    fun getRecentTransactions(): Flow<List<TransactionEntity>>


}