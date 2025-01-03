package com.haf.artha.presentation.transaction.detail

import DateUtils.toFormattedFullDate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.data.local.model.TransactionType
import com.haf.artha.data.model.TransactionDetail
import com.haf.artha.presentation.component.DeleteConfirmationDialog
import com.haf.artha.utils.CurrencyUtils.formatAmount

@Composable
fun DetailTransactionScreen(
    modifier: Modifier = Modifier,
    transactionId: Int,
    navController: NavHostController,
    viewModel: DetailTransactionViewModel = hiltViewModel()
) {
    var transaction: TransactionDetail by remember { mutableStateOf(
        TransactionDetail(
            id = 0,
            accountId = 0,
            categoryId = 0,
            transactionName = "",
            date = 0L,
            type = TransactionType.INCOME,
            toAccountId = null,
            note = "",
            amount = 0.0,
            categoryName = "",
            sendFrom = "",
            sendTo = ""
        )
    ) }
    LaunchedEffect(transaction) {
        if (transaction.id != -1){
            transaction = viewModel.getTransaction(transactionId)
        }
    }
    DetailScreenContent(transaction = transaction!!, navController = navController)

}

@Composable
fun DetailScreenContent(
    modifier: Modifier = Modifier,
    transaction: TransactionDetail?,
    navController: NavHostController,
    viewModel: DetailTransactionViewModel = hiltViewModel()
) {

    var showDeleteConfirmation by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DeleteConfirmationDialog(
            showDialog = showDeleteConfirmation,
            onConfirm = {
                viewModel.deleteTransaction(transaction!!)
                navController.popBackStack()
                showDeleteConfirmation = false
            },
            onDismiss = {
                showDeleteConfirmation = false
            }
        )

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),

        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                transaction?.let {
                    Text(text = it.transactionName, style = MaterialTheme.typography.displaySmall, color = Color.Black)
                    Text(text = "Akun Pembayaran: ${it.sendFrom}")
                    Text(text = "Kategori: ${it.categoryName}")
                    Text(text = "Tanggal Transaksi: ${it.date.toFormattedFullDate()}")
                    val type = when(it.type) {
                        TransactionType.INCOME -> "Pemasukan"
                        TransactionType.EXPENSE -> "Pengeluaran}"
                        else -> "Transfer"
                    }
                    Text(text = "Type: $type")
                    if(it.type == TransactionType.TRANSFER) {
                        Text(text = "Di kirim dari akun ${it.sendFrom} ke akun ${it.sendTo ?: "N/A"}")
                    }

                    Text(text = "Note: ${it.note}")
                    Text(text = "Amount: ${formatAmount(it.amount)}")
                }

            }
        }


        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    showDeleteConfirmation = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            )
            {
                Text("Hapus Transaksi")
            }
        }
    }

}