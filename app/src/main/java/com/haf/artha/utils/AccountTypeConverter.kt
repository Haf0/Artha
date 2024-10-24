package com.haf.artha.utils

import androidx.room.TypeConverter
import com.haf.artha.data.local.model.AccountType

class AccountTypeConverter {
    @TypeConverter
    fun fromAccountType(accountType: AccountType): String {
        return when (accountType) {
            AccountType.E_WALLET -> "E-Wallet"
            AccountType.BANK -> "Bank"
            AccountType.CASH -> "Tunai"
            AccountType.CREDIT_CARD -> "Kartu Kredit"
            AccountType.DEBIT_CARD -> "Katru Debit"
            AccountType.LAINNYA -> "Lainnya"
        }
    }

    @TypeConverter
    fun toAccountType(value: String): AccountType {
        return when (value) {
            "E-Wallet" -> AccountType.E_WALLET
            "Bank" -> AccountType.BANK
            "Tunai" -> AccountType.CASH
            "Kartu Kredit" -> AccountType.CREDIT_CARD
            "Kartu Debit" -> AccountType.DEBIT_CARD
            "Lainnya" -> AccountType.LAINNYA
            else -> throw IllegalArgumentException("Unknown AccountType: $value")
        }
    }
}