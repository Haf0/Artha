@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)

package com.haf.artha.presentation.transaction.list

import DateUtils.toFormattedDate
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.R
import com.haf.artha.navigation.Screen
import com.haf.artha.presentation.category.list.component.positionAwareImePadding
import com.haf.artha.presentation.home.component.TransactionHistoryItem
import com.haf.artha.presentation.transaction.list.component.FilterAccountItem
import com.haf.artha.presentation.transaction.list.component.FilterCategoryItem
import com.haf.artha.presentation.transaction.list.component.FilterTransactionTypeItem
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
    var minAmount by remember { mutableStateOf<Double?>(null) }
    var maxAmount by remember { mutableStateOf<Double?>(null) }
    var accountId by remember { mutableStateOf<Int?>(null) }
    var selectedTransactionTypes by remember { mutableStateOf(listOf<String>()) }
    var selectedCategories by remember { mutableStateOf(listOf<Int>()) }
    val listAccount = viewModel.accounts.collectAsState()
    val listCategories = viewModel.categories.collectAsState().value.sortedBy { it.name }
    val listTransactionTypes = listOf(
        "Pemasukan",
        "Pengeluaran",
        "Transfer"
    )

    val filterState = viewModel.filterState.collectAsState().value

    LaunchedEffect(filterState) {
        startDate = filterState.startDate
        endDate = filterState.endDate
        minAmount = filterState.minAmount
        maxAmount = filterState.maxAmount
        accountId = filterState.accountId
        selectedTransactionTypes = filterState.transactionTypes ?: emptyList()
        selectedCategories = filterState.categories ?: emptyList()
    }

    Log.d("list", "FilterModalContent: $listCategories")
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                .positionAwareImePadding()
                .verticalScroll(rememberScrollState())
        ) {
            val context = LocalContext.current
            Text("Akun", modifier = Modifier.padding(top = 16.dp))
            Spacer(modifier = Modifier.padding(bottom = 4.dp, top = 8.dp, start = 4.dp, end = 4.dp))
            FlowRow {
                listAccount.value.sortedBy { it.name }.forEach { account ->
                    FilterAccountItem(
                        name = account.name,
                        isSelected = accountId == account.id,
                        onClick = {
                            accountId = if (accountId != account.id) { account.id } else{ null }
                        }
                    )
                }
            }
            Text("Kategori", modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.padding(bottom = 4.dp, top = 8.dp, start = 4.dp, end = 4.dp))
            FlowRow {
                listCategories.forEachIndexed { index, category ->
                    FilterCategoryItem(
                        modifier = Modifier.padding(4.dp),
                        color = category.color,
                        name = category.name,
                        isSelected = listCategories[index].id in selectedCategories,
                        onClick = {
                            if (selectedCategories.contains(category.id)) {
                                selectedCategories -= category.id
                            } else {
                                selectedCategories += category.id
                            }

                        }
                    )
                }
            }

            Text("Jenis Transaksi", modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.padding(bottom = 4.dp, top = 8.dp, start = 4.dp, end = 4.dp))
            FlowRow {
                listTransactionTypes.forEachIndexed { index, name ->
                    FilterTransactionTypeItem(
                        name = name,
                        isSelected = listTransactionTypes[index] in selectedTransactionTypes,
                        onClick = {
                            selectedTransactionTypes = if (selectedTransactionTypes.contains(name)) {
                                selectedTransactionTypes - name
                            } else {
                                selectedTransactionTypes + name
                            }
                        }
                    )
                }
            }

            Text("Jumlah Uang", modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.padding(bottom = 4.dp, top = 8.dp, start = 4.dp, end = 4.dp))


            val keyboardController = LocalSoftwareKeyboardController.current
            val focusRequester = remember { FocusRequester() }
            OutlinedTextField(
                value = minAmount?.let{ "%.0f".format(it) } ?: "",
                onValueChange = {
                    minAmount = it.toDoubleOrNull()
                },
                label = { Text("Jumlah Minimal") },
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions {
                  focusRequester.requestFocus()
                }
            )

            OutlinedTextField(
                value = maxAmount?.let{ "%.0f".format(it) } ?: "",
                onValueChange = {
                    maxAmount = it.toDoubleOrNull()
                },
                label = { Text("Jumlah Maksimal") },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .focusRequester(focusRequester),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions {
                    keyboardController?.hide()
                }
            )

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
                        }else{
                            viewModel.updateCategories(emptyList())
                        }
                        if(startDate != null && endDate != null){
                            viewModel.updateDateRange(startDate!!, endDate!!)
                        }
                        if(selectedTransactionTypes.isNotEmpty()){
                            viewModel.updateTransactionTypes(selectedTransactionTypes)
                        }else{
                            viewModel.updateTransactionTypes(emptyList())
                        }
                        if(minAmount != null && maxAmount != null){
                            viewModel.updateAmountRange(minAmount!!, maxAmount!!)
                        }else{
                            if (minAmount == null){
                                viewModel.updateAmountRange(0.0, maxAmount!!)
                            }else{
                                viewModel.updateAmountRange(minAmount!!, null)
                            }
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






