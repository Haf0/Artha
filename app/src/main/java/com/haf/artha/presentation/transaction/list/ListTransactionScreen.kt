@file:OptIn(ExperimentalFoundationApi::class)

package com.haf.artha.presentation.transaction.list

import DateUtils.toFormattedDate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.navigation.Screen
import com.haf.artha.presentation.home.component.TransactionHistoryItem
import com.haf.artha.utils.CurrencyUtils

@Composable
fun ListTransactionScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    /* TODO Filter transactions by date */
    ListTransactionContent(
        navController = navController
    )
}

@Composable
fun ListTransactionContent(
    modifier: Modifier = Modifier,
    viewModel: ListTransactionViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val transactions by viewModel.transactions.collectAsState(emptyList())

    LazyColumn {
        val groupedTransactions = transactions.groupBy { it.date.toFormattedDate() }
        groupedTransactions.forEach { (date, transactions) ->
            stickyHeader {
                Text(
                    text = date,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xD7FFD8E4))
                        .padding(8.dp)
                )
            }
            items(transactions) { transaction ->
                TransactionHistoryItem(
                    title = transaction.name,
                    date = transaction.date.toFormattedDate(),
                    amount = CurrencyUtils.formatAmount(transaction.amount),
                    transactionType = transaction.type,
                    onClickItem = {
                        navController.navigate(Screen.DetailTransaction.createRoute(transaction.transactionId))
                    }
                )
            }
        }
    }
}