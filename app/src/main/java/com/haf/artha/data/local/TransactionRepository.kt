package com.haf.artha.data.local
import DateUtils
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.haf.artha.data.local.db.dao.AccountDao
import com.haf.artha.data.local.db.dao.TransactionDao
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.data.local.model.CategoryAmount
import com.haf.artha.data.local.model.TransactionType
import com.haf.artha.data.model.TransactionDetail
import com.haf.artha.data.model.TransactionFilterState
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

    suspend fun deleteTransaction(transaction: TransactionDetail) {
        transactionDao.deleteById(transaction.id)

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

    suspend fun getTransactionById(transactionId: Int): TransactionDetail {
        return transactionDao.getTransactionDetailById(transactionId)
    }

    fun getTotalIncomeThisMonth(): Flow<Double> {
        val (startOfMonth, endOfMonth) = DateUtils.getStartAndEndOfSpecificMonth(DateUtils.getTodayTimestamp())
        return transactionDao.getTotalIncomeThisMonth(startOfMonth, endOfMonth, TransactionType.INCOME).map { it ?: 0.0 }
    }

    fun getTotalExpenseThisMonth(): Flow<Double> {
        val (startOfMonth, endOfMonth) = DateUtils.getStartAndEndOfSpecificMonth(DateUtils.getTodayTimestamp())
        return transactionDao.getTotalExpenseThisMonth(startOfMonth, endOfMonth).map { it ?: 0.0 }
    }

    fun getRecentTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getRecentTransactions()
    }

    fun filterTransactions(
        transactionFilterState: TransactionFilterState
    ): Flow<List<TransactionEntity>> {
        val endDate = transactionFilterState.endDate ?: DateUtils.getTodayTimestamp()
        val newFilterState = transactionFilterState.copy(endDate = endDate)
        val filteredTransaction = transactionDao.getFilterTransactions(buildFilterTransactionQuery(newFilterState))
        return filteredTransaction

    }


    private fun buildFilterTransactionQuery(
        transactionFilterState: TransactionFilterState
    ): SupportSQLiteQuery{
        val query = StringBuilder("SELECT * FROM transactions WHERE 1=1")
        val args = mutableListOf<Any>()


        if(!transactionFilterState.searchText.isNullOrBlank()){
            query.append(" AND (name LIKE '%' || ? || '%' OR note LIKE '%' || ? || '%')")
            args.add(transactionFilterState.searchText)
            args.add(transactionFilterState.searchText)
        }

        if (transactionFilterState.startDate != null) {
            query.append(" AND date >= ?")
            args.add(DateUtils.getStartOfDay(transactionFilterState.startDate))
        }
        if (transactionFilterState.endDate != null) {
            query.append(" AND date <= ?")
            args.add(DateUtils.getEndOfDay(transactionFilterState.endDate))
        }

        if(!transactionFilterState.transactionTypes.isNullOrEmpty()){
            query.append(" AND type IN (${transactionFilterState.transactionTypes.joinToString(","){"?"}})")
            args.addAll(transactionFilterState.transactionTypes)
        }

        if(!transactionFilterState.categories.isNullOrEmpty()){
            query.append(" AND category_id IN (${transactionFilterState.categories.joinToString(","){"?"}})")
            args.addAll(transactionFilterState.categories)
        }

        if (transactionFilterState.minAmount != null) {
            query.append(" AND amount >= ?")
            args.add(transactionFilterState.minAmount)
        }
        if (transactionFilterState.maxAmount != null) {
            query.append(" AND amount <= ?")
            args.add(transactionFilterState.maxAmount)
        }

        if(transactionFilterState.accountId != null){
            query.append(" AND account_id = ?")
            args.add(transactionFilterState.accountId)
        }

        return SimpleSQLiteQuery(query.toString(), args.toTypedArray())
    }


    fun getCategoryAmount(year: Int,month:Int): Flow<List<CategoryAmount>> {
        val formattedMonth = month.toString().padStart(2, '0')
        return transactionDao.getCategoryAmount("$year",formattedMonth)
    }

    fun getTotalIncomeByMonth(year: Int, month: Int): Flow<Double> {
        val formattedMonth = month.toString().padStart(2, '0')
        return transactionDao.getTotalIncomeByMonth("$year", formattedMonth).map { it ?: 0.0 }
    }
    fun getTotalExpenseByMonth(year: Int, month: Int): Flow<Double> {
        val formattedMonth = month.toString().padStart(2, '0')
        return transactionDao.getTotalExpenseByMonth("$year", formattedMonth).map { it ?: 0.0 }
    }

}
