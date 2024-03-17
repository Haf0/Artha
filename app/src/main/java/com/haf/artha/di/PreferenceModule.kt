package com.haf.artha.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//
//val Context.userDataStore: DataStore<Preferences> by preferencesDataStore("settings")
//
//@Module
//@InstallIn(SingletonComponent::class)
//class PreferenceModule {
//    companion object {
//        @Provides
//        @Singleton
//        fun provideUserDataStorePreferences(
//            applicationContext: Context
//        ): DataStore<Preferences> {
//            return applicationContext.userDataStore
//        }
//    }
//}