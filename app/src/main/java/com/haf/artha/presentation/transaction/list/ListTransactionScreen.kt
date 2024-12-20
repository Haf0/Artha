@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)

package com.haf.artha.presentation.transaction.list

import DateUtils.toFormattedDate
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.R
import com.haf.artha.data.local.model.TransactionType
import com.haf.artha.navigation.Screen
import com.haf.artha.presentation.category.list.component.positionAwareImePadding
import com.haf.artha.presentation.home.component.TransactionHistoryItem
import com.haf.artha.presentation.transaction.list.component.FilterAccountItem
import com.haf.artha.presentation.transaction.list.component.FilterCategpryItem
import com.haf.artha.utils.CurrencyUtils

@Composable
fun ListTransactionScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
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

    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var isFilterExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Transaction History")
                },
                actions = {
                    IconButton(
                        onClick = {
                            isSearchExpanded = !isSearchExpanded
                        }
                    ) {
                        Icon(
                            imageVector = if (isSearchExpanded) {
                                Icons.Filled.Close
                            } else {
                                Icons.Filled.Search
                            },
                            contentDescription = "Search"
                        )
                    }
                }
            )

        }
    ) { paddingValues ->
        val transactions by viewModel.filteredTransactions.collectAsState(emptyList())
        Log.d("ListTransaction", "ListTransactionContent: $transactions")

        FilterModal(
            openBottomSheet = isFilterExpanded,
            onDismissRequest = {
                isFilterExpanded = false
            }
        )
        Column(
            modifier = modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            ) {
                if (isSearchExpanded) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.updateSearchQuery(it)
                        },
                        label = { Text("Cari Transaksi") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
                FilledTonalButton(
                    onClick = {
                        isFilterExpanded = true
                    }
                ) {
                    Icon(
                        modifier = modifier.padding(end = 8.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.filtericon),
                        contentDescription = "Search",

                    )
                    Text(text = "Filter")
                }
                LazyColumn{
                    val groupedTransactions = transactions.groupBy { it.date.toFormattedDate() }
                    groupedTransactions.forEach { (date, transactions) ->
                        stickyHeader {
                            Text(
                                text = date,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFB39DDB))
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

        }
    }
}

@Composable
fun FilterModal(
    openBottomSheet : Boolean,
    onDismissRequest: () -> Unit
) {
    val density = LocalDensity.current
    val bottomSheetState = remember {
        SheetState(
            skipPartiallyExpanded = true,
            density = density
        )
    }

    if (openBottomSheet) {
        FilterModalContent(
            bottomSheetState = bottomSheetState,
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
fun FilterModalContent(
    bottomSheetState: SheetState,
    onDismissRequest: () -> Unit,
    viewModel: ListTransactionViewModel = hiltViewModel()
) {
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }
    var transactionTypes by remember { mutableStateOf<List<TransactionType>?>(null) }
    var minAmount by remember { mutableStateOf<Double?>(null) }
    var maxAmount by remember { mutableStateOf<Double?>(null) }
    var accountId by remember { mutableStateOf<Int?>(null) }

    val selectedCategories = mutableListOf<Int>()
    val listAccount = viewModel.accounts.collectAsState()
    val listCategories = viewModel.categories.collectAsState()
    val listTransactionTypes = listOf(
        Pair("Pemasukan", TransactionType.INCOME),
        Pair("Pengeluaran", TransactionType.EXPENSE),
        Pair("Transfer", TransactionType.TRANSFER)
    )
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                .positionAwareImePadding()
        ) {
            val context = LocalContext.current
            Text("Akun")
            Spacer(modifier = Modifier.padding(4.dp))
            FlowRow {
                listAccount.value.sortedBy { it.name }.forEach { account ->
                    FilterAccountItem(
                        name = account.name,
                        isSelected = accountId == account.id,
                        onClick = {
                            accountId = account.id
                            Toast.makeText(context, "${account.id}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
            Text("Kategori")
            Spacer(modifier = Modifier.padding(4.dp))
            FlowRow {
                repeat(listCategories.value.size) { index ->
                    FilterCategpryItem(
                        list = listCategories.value.sortedBy { it.name }.map { it }.toMutableList(),
                        index = index,
                        onClick = {
                            if (selectedCategories.contains(listCategories.value[index].id)) {
                                selectedCategories.remove(listCategories.value[index].id)
                            } else {
                                selectedCategories.add(listCategories.value[index].id)
                            }
                        }
                    )
                }
            }




            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp, top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(
                    modifier = Modifier,
                    onClick = {
                        viewModel.clearFilters()
                        onDismissRequest()
                    }
                ) {
                    Text("Hapus Filter")
                }

                Button(
                    modifier = Modifier,
                    onClick = {
                        if(selectedCategories.isNotEmpty()){
                            viewModel.updateCategories(selectedCategories)
                        }
                        if(startDate != null && endDate != null){
                            viewModel.updateDateRange(startDate!!, endDate!!)
                        }
                        if(transactionTypes != null){
                            viewModel.updateTransactionTypes(transactionTypes!!)
                        }
                        if(minAmount != null && maxAmount != null){
                            viewModel.updateAmountRange(minAmount!!, maxAmount!!)
                        }
                        if(accountId != null){
                            viewModel.updateAccountId(accountId!!)
                        }
                        onDismissRequest()
                    }
                ) {
                    Text("Terapkan Filter")
                }
            }

        }
    }
}






