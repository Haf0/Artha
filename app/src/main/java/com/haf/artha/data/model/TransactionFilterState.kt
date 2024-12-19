package com.haf.artha.data.model

import com.haf.artha.data.local.model.TransactionType

data class TransactionFilterState(
    val searchText: String? = null,
    val startDate: Long?= null,
    val endDate: Long? = null,
    val transactionTypes: List<TransactionType>? = null,
    val categories: List<Int>? = null,
    val minAmount: Double? = null,
    val maxAmount: Double? = null,
    val accountId: Int? = null
)
