package com.haf.artha.utils

import androidx.room.TypeConverter
import com.haf.artha.data.local.model.AccountType

class AccountTypeConverter {
    @TypeConverter
    fun fromAccountType(accountType: AccountType): String {
        return when (accountType) {
            AccountType.E_WALLET -> "E-Wallet"
            AccountType.BANK -> "Bank"
            AccountType.CASH -> "Cash"
        }
    }

    @TypeConverter
    fun toAccountType(value: String): AccountType {
        return when (value) {
            "E-Wallet" -> AccountType.E_WALLET
            "Bank" -> AccountType.BANK
            "Cash" -> AccountType.CASH
            else -> throw IllegalArgumentException("Unknown AccountType: $value")
        }
    }
}