package com.haf.artha.presentation.transaction.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun ListTransactionScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    ListTransactionContent()
}

@Composable
fun ListTransactionContent(
    modifier: Modifier = Modifier
) {
    Text("List Transaction")
}