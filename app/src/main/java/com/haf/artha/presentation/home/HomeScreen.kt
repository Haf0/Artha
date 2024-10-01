package com.haf.artha.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.haf.artha.data.local.entity.TransactionEntity
import com.haf.artha.presentation.home.component.BalanceItem
import com.haf.artha.presentation.home.component.IncomeOutcomeItem
import com.haf.artha.presentation.home.component.TransactionHistoryItem
import com.haf.artha.ui.theme.PurpleGrey40

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    HomeScreenContent(
        modifier = modifier,
        onNavigateToListTransaction = {
            //TODO navigate to list transaction
        },
        onNavigateToDetailTransaction = {
            //TODO navigate to detail transaction
        },
        totalBalance = "Rp 10000000",
        income = "Rp 10000000",
        outcome = "Rp 10000000",
        listHistoryTransaction = listOf()
    )
}


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
            items(listHistoryTransaction){
                TransactionHistoryItem(
                    /*TODO change it.categoruId to category color*/
                    intColor = it.categoryId,
                    title = it.transactionName,
                    date = "${it.transactionDayOfWeek}, ${it.transactionDay} ${it.transactionMonth} ${it.transactionYear}",
                    amount = it.transactionAmount.toString()
                )
            }
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    
    val listDummyTransaction = listOf<TransactionEntity>(
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,
            
        ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            ),
        TransactionEntity(
            transactionId = 1,
            accountId = 1,
            categoryId = 1,
            transactionName = "Transaction 1",
            transactionDayOfWeek = "Rabu",
            transactionDay = 1,
            transactionMonth = 12,
            transactionYear = 2021,
            transactionType = "INCOME",
            transactionNote = "Transaction 1",
            transactionAmount = 1000000.0,

            )
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