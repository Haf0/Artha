package com.haf.artha.data.local
import DateUtils
import com.haf.artha.data.local.db.AccountDao
import com.haf.artha.data.local.db.TransactionDao
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.data.local.model.TransactionType
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao
) {

    // Insert a transaction and update the account balance accordingly
    suspend fun insertTransaction(transaction: TransactionEntity) {
        val newTransactionId = transactionDao.insert(transaction)

        // Update the account balance based on the transaction type
        val account = accountDao.getAccountById(transaction.accountId)
        if (account != null) {
            when (transaction.transactionType) {
                TransactionType.INCOME -> {
                    val updatedBalance = account.balance + transaction.transactionAmount
                    accountDao.update(account.copy(balance = updatedBalance))
                }
                TransactionType.EXPENSE -> {
                    val updatedBalance = account.balance - transaction.transactionAmount
                    accountDao.update(account.copy(balance = updatedBalance))
                }
                TransactionType.TRANSFER -> {
                    val sourceAccount = accountDao.getAccountById(transaction.accountId)
                    val destinationAccount = accountDao.getAccountById(transaction.categoryId) // Assuming categoryId refers to destination account

                    if (sourceAccount != null && destinationAccount != null) {
                        val updatedSourceBalance = sourceAccount.balance - transaction.transactionAmount
                        val updatedDestinationBalance = destinationAccount.balance + transaction.transactionAmount

                        accountDao.update(sourceAccount.copy(balance = updatedSourceBalance))
                        accountDao.update(destinationAccount.copy(balance = updatedDestinationBalance))
                    }
                }
            }
        }
    }

    // Update a transaction and adjust the account balance
    suspend fun updateTransaction(transaction: TransactionEntity) {
        val oldTransaction = transaction.transactionId?.let {
            transactionDao.getTransactionById(it)
        }


        if (oldTransaction != null) {
            // Revert the account balance for the old transaction
            val account = accountDao.getAccountById(oldTransaction.accountId)
            if (account != null) {
                when (oldTransaction.transactionType) {
                    TransactionType.INCOME -> {
                        accountDao.update(account.copy(balance = account.balance - oldTransaction.transactionAmount))
                    }
                    TransactionType.EXPENSE -> {
                        accountDao.update(account.copy(balance = account.balance + oldTransaction.transactionAmount))
                    }
                    TransactionType.TRANSFER -> {
                        val sourceAccount = accountDao.getAccountById(oldTransaction.accountId)
                        val destinationAccount = accountDao.getAccountById(oldTransaction.categoryId)

                        if (sourceAccount != null && destinationAccount != null) {
                            accountDao.update(sourceAccount.copy(balance = sourceAccount.balance + oldTransaction.transactionAmount))
                            accountDao.update(destinationAccount.copy(balance = destinationAccount.balance - oldTransaction.transactionAmount))
                        }
                    }
                }
            }

            // Update the transaction
            transactionDao.update(transaction)

            // Update the account balance for the new transaction
            insertTransaction(transaction)
        }
    }

    // Delete a transaction and update the account balance accordingly
    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.delete(transaction)

        val account = accountDao.getAccountById(transaction.accountId)
        if (account != null) {
            when (transaction.transactionType) {
                TransactionType.INCOME -> {
                    val updatedBalance = account.balance - transaction.transactionAmount
                    accountDao.update(account.copy(balance = updatedBalance))
                }
                TransactionType.EXPENSE -> {
                    val updatedBalance = account.balance + transaction.transactionAmount
                    accountDao.update(account.copy(balance = updatedBalance))
                }
                TransactionType.TRANSFER -> {
                    val sourceAccount = accountDao.getAccountById(transaction.accountId)
                    val destinationAccount = accountDao.getAccountById(transaction.categoryId)

                    if (sourceAccount != null && destinationAccount != null) {
                        val updatedSourceBalance = sourceAccount.balance + transaction.transactionAmount
                        val updatedDestinationBalance = destinationAccount.balance - transaction.transactionAmount

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


}
