package com.haf.artha.data.local.model

import com.haf.artha.data.local.entity.TransactionEntity

data class CombinedHomeState(
    val totalBalance: Double,
    val totalIncome: Double,
    val totalExpense: Double,
    val recentTransaction: List<TransactionEntity>
)
