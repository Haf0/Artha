package com.haf.artha.data.model

data class TransactionFilterState(
    val searchText: String? = null,
    val startDate: Long?= null,
    val endDate: Long? = null,
    val transactionTypes: List<String>? = null,
    val categories: List<Int>? = null,
    val minAmount: Double? = null,
    val maxAmount: Double? = null,
    val accountId: Int? = null
)
