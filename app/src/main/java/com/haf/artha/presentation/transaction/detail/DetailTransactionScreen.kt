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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.data.local.model.TransactionType

@Composable
fun DetailTransactionScreen(
    modifier: Modifier = Modifier,
    transactionId: Int,
    navController: NavHostController,
    viewModel: DetailTransactionViewModel = hiltViewModel()
) {
    var transaction: TransactionEntity by remember { mutableStateOf(TransactionEntity(0,0,0,"",0L,TransactionType.INCOME,0,"",0.0)) }
    LaunchedEffect(transaction) {
        transaction = viewModel.getTransaction(transactionId)
    }
    DetailScreenContent(transaction = transaction)

    /* TODO Add edit and delete button and make pretty*/
}

@Composable
fun DetailScreenContent(
    modifier: Modifier = Modifier,
    transaction: TransactionEntity
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Transaction Details", style = MaterialTheme.typography.displayMedium)
            Text(text = "ID: ${transaction.transactionId}")
            Text(text = "Account ID: ${transaction.accountId}")
            Text(text = "Category ID: ${transaction.categoryId}")
            Text(text = "Name: ${transaction.name}")
            Text(text = "Date: ${transaction.date.toFormattedDate()}")
            Text(text = "Type: ${transaction.type}")
            Text(text = "To Account ID: ${transaction.toAccountId ?: "N/A"}")
            Text(text = "Note: ${transaction.note}")
            Text(text = "Amount: ${transaction.amount}")
        }
    }
}