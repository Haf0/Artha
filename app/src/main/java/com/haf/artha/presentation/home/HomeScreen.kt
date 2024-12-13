@file:OptIn(ExperimentalFoundationApi::class)

package com.haf.artha.presentation.home

import DateUtils.toFormattedDate
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.data.local.model.TransactionType
import com.haf.artha.navigation.Screen
import com.haf.artha.presentation.common.UiState
import com.haf.artha.presentation.component.LoadingIndicator
import com.haf.artha.presentation.home.component.BalanceItem
import com.haf.artha.presentation.home.component.IncomeOutcomeItem
import com.haf.artha.presentation.home.component.TransactionHistoryItem
import com.haf.artha.ui.theme.PurpleGrey40
import com.haf.artha.utils.CurrencyUtils

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val homeState by viewModel.uiState.collectAsState()
    when(homeState){
        is UiState.Loading -> {
            LoadingIndicator()
        }
        is UiState.Error -> {
            Log.d("homeScreen", "HomeScreen: loading")
        }
        is UiState.Success -> {
            Log.d("homeScreen", "HomeScreen: ${(homeState as UiState.Success).data}")
            HomeScreenContent(
                modifier = modifier,
                onNavigateToListTransaction = {
                    navController.navigate(Screen.Transaction.route)
                },
                onNavigateToDetailTransaction = {
                    navController.navigate(Screen.DetailTransaction.createRoute(it))
                },
                totalBalance = CurrencyUtils.formatAmount((homeState as UiState.Success).data.totalBalance),
                income = CurrencyUtils.formatAmount((homeState as UiState.Success).data.totalIncome),
                outcome = CurrencyUtils.formatAmount((homeState as UiState.Success).data.totalExpense),
                listHistoryTransaction = (homeState as UiState.Success).data.recentTransaction
            )
        }
    }

}


@ExperimentalFoundationApi
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onNavigateToListTransaction : () -> Unit,
    onNavigateToDetailTransaction : (Int) -> Unit,
    totalBalance : String,
    income : String,
    outcome : String,
    listHistoryTransaction : List<TransactionEntity>
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        BalanceItem(
            balance = totalBalance
        )
        IncomeOutcomeItem(
            income = income,
            outcome = outcome
        )
        HorizontalDivider(color = PurpleGrey40, thickness = 1.dp, modifier = Modifier.padding(top = 16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Riwayat Transaksi")
            Spacer(Modifier.weight(1f))
            TextButton(
                onClick = {
                    onNavigateToListTransaction()
                }
            ) {
                Text(text = "Lihat Semua")
            }
        }
        HorizontalDivider(color = PurpleGrey40, thickness = 1.dp, modifier = Modifier.padding(bottom = 8.dp))

        LazyColumn {
            val groupedTransactions = listHistoryTransaction.groupBy { it.date.toFormattedDate() }
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
                            onNavigateToDetailTransaction(transaction.transactionId)
                        }
                    )
                }
            }
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    
    val listDummyTransaction = listOf(
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            name = "Transaction 1",
            date = 1703980800000,
            type = TransactionType.INCOME,
            toAccountId = 1,
            note = "Transaction 1",
            amount = 1000000.0,
        ),

    )
    HomeScreenContent(
        onNavigateToListTransaction = {},
        onNavigateToDetailTransaction = {},
        totalBalance = "Rp 10000000",
        income = "Rp 10000000",
        outcome = "Rp 10000000",
        listHistoryTransaction = listDummyTransaction
    )
}