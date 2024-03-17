package com.haf.artha.presentation.onboarding.setCurrency

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SetCurrencyViewModel @Inject constructor(): ViewModel(){



    private val _groupedCurrency = MutableStateFlow(
        getAvailableCurrency()
            .sortedBy { it.code }

    )
    val groupedCurrency: MutableStateFlow<List<AvailableCurrency>> get() = _groupedCurrency

}