package com.haf.artha.presentation.transaction.detail

import DateUtils.toFormattedDate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.data.local.model.TransactionType
import com.haf.artha.data.model.TransactionDetail
import com.haf.artha.utils.CurrencyUtils.formatAmount

@Composable
fun DetailTransactionScreen(
    modifier: Modifier = Modifier,
    transactionId: Int,
    navController: NavHostController,
    viewModel: DetailTransactionViewModel = hiltViewModel()
) {
    var transaction: TransactionDetail by remember { mutableStateOf(TransactionDetail(0, "", 0L, TransactionType.INCOME, "", 0.0, "", "", "")) }
    LaunchedEffect(transaction) {
        transaction = viewModel.getTransaction(transactionId)
    }
    DetailScreenContent(transaction = transaction)

    /* TODO Add edit and delete button and make pretty*/
}

@Composable
fun DetailScreenContent(
    modifier: Modifier = Modifier,
    transaction: TransactionDetail
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = transaction.transactionName, style = MaterialTheme.typography.displaySmall, color = Color.Black)
            Text(text = "Account: ${transaction.sendFrom}")
            Text(text = "Category: ${transaction.categoryName}")
            Text(text = "Date: ${transaction.date.toFormattedDate()}")
            val type = when(transaction.type) {
                TransactionType.INCOME -> "Pemasukan"
                TransactionType.EXPENSE -> "Pengeluaran}"
                else -> "Transfer"
            }
            Text(text = "Type: $type")
            Text(text = "To Account ID: ${transaction.sendTo ?: "N/A"}")
            Text(text = "Note: ${transaction.note}")
            Text(text = "Amount: ${formatAmount(transaction.amount)}")
        }
    }
}