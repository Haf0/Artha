package com.haf.artha.data.local
import DateUtils
import com.haf.artha.data.local.db.dao.AccountDao
import com.haf.artha.data.local.db.dao.TransactionDao
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.data.local.model.TransactionType
import com.haf.artha.data.model.TransactionDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao
) {

    suspend fun insertTransaction(transaction: TransactionEntity) { // Update the account balance based on the transaction type
        val account = accountDao.getAccountById(transaction.accountId)
        transactionDao.insert(transaction)
        if (account != null) {
            when (transaction.type) {
                TransactionType.INCOME -> {
                    val updatedBalance = account.balance + transaction.amount
                    accountDao.update(account.copy(balance = updatedBalance))
                }
                TransactionType.EXPENSE -> {
                    val updatedBalance = account.balance - transaction.amount
                    accountDao.update(account.copy(balance = updatedBalance))
                }
                TransactionType.TRANSFER -> {
                    val sourceAccount = accountDao.getAccountById(transaction.accountId)
                    val destinationAccount = transaction.toAccountId?.let { accountDao.getAccountById(it) }
                    if (sourceAccount != null && destinationAccount != null) {
                        val updatedSourceBalance = sourceAccount.balance - transaction.amount
                        val updatedDestinationBalance = destinationAccount.balance + transaction.amount

                        accountDao.update(sourceAccount.copy(balance = updatedSourceBalance))
                        accountDao.update(destinationAccount.copy(balance = updatedDestinationBalance))
                    }
                }
            }
        }
    }
    /* TODO need to change delete the transaction by ID then insert the as the new transaction*/
    suspend fun updateTransaction(transaction: TransactionEntity) {
        val oldTransaction = transaction.transactionId.let {
            transactionDao.getTransactionById(it)
        }

        // Revert the account balance for the old transaction
        val account = accountDao.getAccountById(oldTransaction.accountId)
        if (account != null) {
            when (oldTransaction.type) {
                TransactionType.INCOME -> {
                    accountDao.update(account.copy(balance = account.balance - oldTransaction.amount))
                }
                TransactionType.EXPENSE -> {
                    accountDao.update(account.copy(balance = account.balance + oldTransaction.amount))
                }
                TransactionType.TRANSFER -> {
                    val sourceAccount = accountDao.getAccountById(oldTransaction.accountId)
                    val destinationAccount = accountDao.getAccountById(oldTransaction.categoryId)

                    if (sourceAccount != null && destinationAccount != null) {
                        accountDao.update(sourceAccount.copy(balance = sourceAccount.balance + oldTransaction.amount))
                        accountDao.update(destinationAccount.copy(balance = destinationAccount.balance - oldTransaction.amount))
                    }
                }
            }
        }

        // Update the transaction
        transactionDao.update(transaction)

        // Update the account balance for the new transaction
        insertTransaction(transaction)
    }

    // Delete a transaction and update the account balance accordingly
    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.delete(transaction)

        val account = accountDao.getAccountById(transaction.accountId)
        if (account != null) {
            when (transaction.type) {
                TransactionType.INCOME -> {
                    val updatedBalance = account.balance - transaction.amount
                    accountDao.update(account.copy(balance = updatedBalance))
                }
                TransactionType.EXPENSE -> {
                    val updatedBalance = account.balance + transaction.amount
                    accountDao.update(account.copy(balance = updatedBalance))
                }
                TransactionType.TRANSFER -> {
                    val sourceAccount = accountDao.getAccountById(transaction.accountId)
                    val destinationAccount = transaction.toAccountId?.let { accountDao.getAccountById(it) }

                    if (sourceAccount != null && destinationAccount != null) {
                        val updatedSourceBalance = sourceAccount.balance + transaction.amount
                        val updatedDestinationBalance = destinationAccount.balance - transaction.amount

                        accountDao.update(sourceAccount.copy(balance = updatedSourceBalance))
                        accountDao.update(destinationAccount.copy(balance = updatedDestinationBalance))
                    }
                }
            }
        }
    }

    // Fetch all transactions
    fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }

    // Fetch a transaction by ID
    suspend fun getTransactionById(transactionId: Int): TransactionDetail {
        return transactionDao.getTransactionDetailById(transactionId)
    }

    // Fetch transactions for a specific day
    fun getTransactionsByDay(timestamp: Long): Flow<List<TransactionEntity>> {
        val (startOfDay, endOfDay) = DateUtils.getStartAndEndOfSpecificDay(timestamp)
        return transactionDao.getTransactionsBySpecificDay(startOfDay, endOfDay)
    }

    // Fetch transactions for a specific month
    fun getTransactionsByMonth(timestamp: Long): Flow<List<TransactionEntity>> {
        val (startOfMonth, endOfMonth) = DateUtils.getStartAndEndOfSpecificMonth(timestamp)
        return transactionDao.getTransactionsBySpecificMonth(startOfMonth, endOfMonth)
    }

    // Fetch transactions for a this week
    fun getTransactionsByWeek(timestamp: Long): Flow<List<TransactionEntity>> {
        val (startOfWeek, endOfWeek) = DateUtils.getStartAndEndOfCurrentWeek(timestamp)
        return transactionDao.getTransactionsBySpecificWeek(startOfWeek, endOfWeek)
    }

    // Fetch transactions for a last week
    fun getTransactionsByLastWeek(timestamp: Long): Flow<List<TransactionEntity>> {
        val (startOfWeek, endOfWeek) = DateUtils.getStartAndEndOfLastWeek(timestamp)
        return transactionDao.getTransactionsBySpecificWeek(startOfWeek, endOfWeek)
    }

    // Fetch the total income for this month
    fun getTotalIncomeThisMonth(): Flow<Double> {
        val (startOfMonth, endOfMonth) = DateUtils.getStartAndEndOfSpecificMonth(DateUtils.getTodayTimestamp())
        return transactionDao.getTotalIncomeThisMonth(startOfMonth, endOfMonth, TransactionType.INCOME).map { it ?: 0.0 }
    }

    // Fetch the total expense for this month
    fun getTotalExpenseThisMonth(): Flow<Double> {
        val (startOfMonth, endOfMonth) = DateUtils.getStartAndEndOfSpecificMonth(DateUtils.getTodayTimestamp())
        return transactionDao.getTotalExpenseThisMonth(startOfMonth, endOfMonth).map { it ?: 0.0 }
    }

    fun getRecentTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getRecentTransactions()
    }
}
