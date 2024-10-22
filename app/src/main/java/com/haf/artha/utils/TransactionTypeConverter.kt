package com.haf.artha.utils

import androidx.room.TypeConverter
import com.haf.artha.data.local.model.TransactionType

class TransactionTypeConverter {
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
}