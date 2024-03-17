package com.haf.artha.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//class PreferenceManager @Inject constructor(private val dataStore: DataStore<Preferences>){
//    fun getOnboardingStatus(): Flow<Boolean> {
//        return dataStore.data.map {
//            it[IS_ONBOARDING_COMPLETED] ?: false
//        }
//    }
//
//    fun getOnboardingStep(): Flow<Int> {
//        return dataStore.data.map {
//            it[ONBOARDING_STEP] ?: 0
//        }
//    }
//
//    suspend fun setOnboardingStep(step: Int) {
//        dataStore.edit {
//            it[ONBOARDING_STEP] = step
//            if (step == 3) {
//                it[IS_ONBOARDING_COMPLETED] = true
//            }
//        }
//
//    }
//
//    companion object{
//
//        private val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
//        private val ONBOARDING_STEP = intPreferencesKey("onboarding_step")
//
//    }
//}