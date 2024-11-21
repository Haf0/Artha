package com.haf.artha.utils

import androidx.room.TypeConverter
import com.haf.artha.data.local.model.TransactionType

class TransactionTypeConverter {
    @TypeConverter
    fun fromTransactionType(transactionType: TransactionType): String {
        return when (transactionType) {
            TransactionType.INCOME -> "Pendapatan"
            TransactionType.EXPENSE -> "Pengeluaran"
            TransactionType.TRANSFER -> "Transfer"
        }
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return when (value) {
            "Pendapatan" -> TransactionType.INCOME
            "Pengeluaran" -> TransactionType.EXPENSE
            "Transfer" -> TransactionType.TRANSFER
            else -> throw IllegalArgumentException("Unknown TransactionType: $value")
        }
    }
}