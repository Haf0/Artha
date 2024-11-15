package com.haf.artha.data.local
import DateUtils
import com.haf.artha.data.local.db.AccountDao
import com.haf.artha.data.local.db.CategoryDao
import com.haf.artha.data.local.db.TransactionDao
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.data.local.model.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao
) {

    suspend fun insertTransaction(transaction: TransactionEntity) { // Update the account balance based on the transaction type
        val account = accountDao.getAccountById(transaction.accountId)
        transactionDao.insert(transaction)
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

    //tranfer transaction function
    suspend fun transferFunds(
        fromWalletId: Int,
        toWalletId: Int,
        amount: Double,
        date: Long,
        note: String
    ) {
        val transferCategory = categoryDao.getCategoryByName("Transfer") // Assuming you have a method to get category by name
        val transferCategoryId = transferCategory?.id ?: 0

        val outgoingTransaction = TransactionEntity(
            transactionId = 0,
            accountId = fromWalletId,
            categoryId = transferCategoryId,
            transactionName  = "Transfer Out",
            transactionDate = date,
            transactionType = TransactionType.EXPENSE,
            transactionNote = note,
            transactionAmount = amount
        )
        val incomingTransaction = TransactionEntity(
            transactionId = 0,
            accountId = toWalletId,
            categoryId = transferCategoryId,
            transactionName  = "Transfer In",
            transactionDate = date,
            transactionType = TransactionType.INCOME,
            transactionNote = note,
            transactionAmount = amount
        )
        transactionDao.insert(outgoingTransaction)
        transactionDao.insert(incomingTransaction)
    }
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
