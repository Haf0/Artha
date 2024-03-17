package com.haf.artha.preference

import android.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class PreferenceViewModel @Inject constructor(private val preference: PreferenceManager): ViewModel(){
//    fun saveOnboardingState(step: Int){
//        viewModelScope.launch {
//            preference.setOnboardingStep(step)
//        }
//    }
//
//    fun getOnboardingStep() = preference.getOnboardingStep()
//    fun getOnboardingStatus() = preference.getOnboardingStatus()
//
//
//}