package com.haf.artha.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.haf.artha.data.local.model.TransactionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionDetail(
    val id: Int,
    @ColumnInfo(name = "transaction_name")
    val transactionName: String,
    val date: Long,
    val type: TransactionType,
    val note: String?,
    val amount: Double,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "send_from")
    val sendFrom: String,
    @ColumnInfo(name = "send_to")
    val sendTo: String?
)
: Parcelable