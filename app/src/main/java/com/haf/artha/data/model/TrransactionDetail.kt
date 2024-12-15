package com.haf.artha.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.haf.artha.data.local.model.TransactionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionDetail(
    val id: Int,
    @ColumnInfo(name = "account_id")
    val accountId: Int,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @ColumnInfo(name = "name")
    val transactionName: String,
    val date: Long,
    val type: TransactionType,
    @ColumnInfo(name = "to_account_id")
    val toAccountId: Int?,
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