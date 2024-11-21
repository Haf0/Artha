package com.haf.artha.di

import com.haf.artha.data.local.AccountRepository
import com.haf.artha.data.local.CategoryRepository
import com.haf.artha.data.local.TransactionRepository
import com.haf.artha.data.local.db.dao.AccountDao
import com.haf.artha.data.local.db.dao.CategoryDao
import com.haf.artha.data.local.db.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun transactionRepository(
        transactionDao: TransactionDao,
        accountDao: AccountDao
    ): TransactionRepository {
        return TransactionRepository(transactionDao, accountDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(accountDao: AccountDao): AccountRepository {
        return AccountRepository(accountDao)
    }
}