package com.haf.artha.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.data.local.model.CategoryAmount
import com.haf.artha.data.local.model.TransactionType
import com.haf.artha.data.local.model.TypeAmount
import com.haf.artha.data.model.TransactionDetail
import kotlinx.coroutines.flow.Flow


@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteById(transactionId: Int)

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

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND date BETWEEN :startOfMonth AND :endOfMonth")
    fun getTotalIncomeThisMonth(startOfMonth: Long, endOfMonth: Long, type:TransactionType): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Pengeluaran' AND date BETWEEN :startOfMonth AND :endOfMonth")
    fun getTotalExpenseThisMonth(startOfMonth: Long, endOfMonth: Long): Flow<Double?>

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT 15")
    fun getRecentTransactions(): Flow<List<TransactionEntity>>

    @Query("""
        SELECT 
            t.*,
            c.name AS category_name,
            a.name AS send_from,
            at.name AS send_to
        FROM 
            transactions t
        JOIN 
            categories c 
            ON t.category_id = c.id
        JOIN 
            account a 
            ON t.account_id = a.id
        LEFT JOIN 
            account at 
            ON t.to_account_id = at.id
        WHERE 
            t.id = :id;

    """)
    suspend fun getTransactionDetailById(id: Int): TransactionDetail

    @RawQuery(observedEntities = [TransactionEntity::class])
    fun getFilterTransactions(query: SupportSQLiteQuery): Flow<List<TransactionEntity>>


    @Query("""
        SELECT 
            c.id as categoryId,
            c.name as categoryName,
            c.color as categoryColor,
            strftime('%Y', datetime(t.date / 1000, 'unixepoch', 'localtime')) as year,
            strftime('%m', datetime(t.date / 1000, 'unixepoch', 'localtime')) as month,
            SUM(t.amount) as totalAmount 
        FROM 
            transactions t
        JOIN 
            categories c ON t.category_id = c.id 
        WHERE 
            strftime('%Y', datetime(t.date / 1000, 'unixepoch', 'localtime')) = :year
            AND strftime('%m', datetime(t.date / 1000, 'unixepoch', 'localtime')) = :month
        GROUP BY 
            t.category_id, year, month
    """)
    fun getCategoryAmount(year: String, month: String): Flow<List<CategoryAmount>>

    @Query("""
        SELECT 
            t.type as type,
            strftime('%Y', datetime(t.date / 1000, 'unixepoch', 'localtime')) as year,
            strftime('%m', datetime(t.date / 1000, 'unixepoch', 'localtime')) as month,
            SUM(t.amount) as totalAmount 
        FROM 
            transactions t
        WHERE 
            year =  :year AND month = :month
        GROUP BY 
            t.type, year, month
    """)
    fun getTypeAmount(year:Int,month: Int): Flow<List<TypeAmount>>



    @Query("""
        SELECT SUM(amount) 
        FROM transactions 
        WHERE type = :type 
        AND strftime('%Y', datetime(date / 1000, 'unixepoch', 'localtime')) = :year 
        AND strftime('%m', datetime(date / 1000, 'unixepoch', 'localtime')) = :month
    """)
    fun getTotalIncomeByMonth(year: String, month: String, type: TransactionType = TransactionType.INCOME): Flow<Double?>

    @Query("""
        SELECT SUM(amount) 
        FROM transactions 
        WHERE type = :type 
        AND strftime('%Y', datetime(date / 1000, 'unixepoch', 'localtime')) = :year 
        AND strftime('%m', datetime(date / 1000, 'unixepoch', 'localtime')) = :month
    """)
    fun getTotalExpenseByMonth(year: String, month: String, type: TransactionType = TransactionType.EXPENSE): Flow<Double?>


}