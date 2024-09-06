package com.haf.artha.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountWithTransaction (
    val id: Int,
    val name: String,
    val accountType : String,
    val balance: Double,
    val totalIncome: Double,
    val totalExpense: Double
): Parcelable