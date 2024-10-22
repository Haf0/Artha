package com.haf.artha.data.local.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.haf.artha.data.local.entity.AccountEntity
import com.haf.artha.data.local.entity.CategoryEntity
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.utils.TransactionTypeConverter

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(TransactionTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
}