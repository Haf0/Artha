package com.haf.artha.presentation.onboarding.setCurrency

import android.icu.util.Currency

data class AvailableCurrency(
    val code: String,
    val name: String
)

fun getAvailableCurrency(): List<AvailableCurrency>{
    var currency = mutableListOf<AvailableCurrency>()
    Currency.getAvailableCurrencies().forEach{
        currency.add(AvailableCurrency(it.currencyCode, it.displayName))
    }
    return currency.sortedBy { it.code }
}
fun searchCurrency(query: String): List<AvailableCurrency>{
    var currency = mutableListOf<AvailableCurrency>()
    Currency.getAvailableCurrencies().forEach{
        currency.add(AvailableCurrency(it.currencyCode, it.displayName))
    }
    return currency
        .filter { it.code.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true) }.sortedBy { it.code }
}
fun getDefaultCurrency(): AvailableCurrency {
    return AvailableCurrency(Currency.getInstance("IDR").currencyCode, Currency.getInstance("IDR").displayName)
}