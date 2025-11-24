package com.haf.artha.preference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferenceViewModel @Inject constructor(private val preferenceManager: PreferenceManager): ViewModel(){
    fun checkOnboardingStatus(): Flow<Pair<Boolean, Int>> {
        return combine(
            preferenceManager.getOnboardingStatus(),
            preferenceManager.getOnboardingStep()
        ) { isCompleted, step ->
            Pair(isCompleted, step)
        }
    }

    private fun setOnboardingCompleted() {
        viewModelScope.launch {
            preferenceManager.setHasCompletedOnboarding(true)
        }
    }

    fun setCurrentStep(step: Int) {
        viewModelScope.launch {
            preferenceManager.setOnboardingStep(step)
        }
    }

    fun completeOnboarding() {
        setOnboardingCompleted()
        setCurrentStep(0)
    }


    fun getUsername(): Flow<String> {
        return preferenceManager.getUsername()
    }

    fun setUsername(username: String) {
        viewModelScope.launch {
            preferenceManager.setUsername(username)
        }
    }

}