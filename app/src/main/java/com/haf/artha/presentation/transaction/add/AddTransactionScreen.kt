@file:OptIn(ExperimentalMaterial3Api::class)

package com.haf.artha.presentation.transaction.add

import DateUtils.getTodayTimestamp
import DateUtils.toFormattedDate
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.data.local.entity.AccountEntity
import com.haf.artha.data.local.entity.CategoryEntity
import com.haf.artha.data.local.model.TransactionType
import com.haf.artha.presentation.common.UiState
import com.haf.artha.presentation.component.LoadingIndicator
import com.haf.artha.presentation.transaction.component.DatePickerModal
import com.haf.artha.utils.CurrencyUtils


val TAG = "AddTransactionScreen"


@Composable
fun AddTransactionScreen(
    navController: NavHostController,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(UiState.Loading)
    when (uiState) {
        is UiState.Success -> {
            val (categories, accounts) = (uiState as UiState.Success).data
            AddTransactionContent(
                categories.filter { it.name != "Transfer" },
                accounts,
                viewModel::addTransaction,
                viewModel::transferFunds,
                onBack = {
                    navController.popBackStack()
                },
                LocalContext.current
            )
        }
        is UiState.Error -> {
            // Handle Error
        }
        is UiState.Loading -> {
            LoadingIndicator()
        }
    }
}

@Composable
fun AddTransactionContent(
    categories: List<CategoryEntity>,
    accounts: List<AccountEntity>,
    insertTransaction: (TransactionType, String, Int, Int, Long, String, Double) -> Unit,
    transferFunds: (AccountEntity,AccountEntity, Double, Long) -> Unit,
    onBack: () -> Unit,
    context: Context
) {
    var transactionType by remember { mutableStateOf(TransactionType.INCOME) }
    var transactionName by remember { mutableStateOf("") }
    var selectedWallet by remember { mutableStateOf(accounts.firstOrNull() ?: AccountEntity(0, "Bank", "Bank", 100000.0)) }
    var transactionDate by remember { mutableLongStateOf(getTodayTimestamp()) }
    var transactionNote by remember { mutableStateOf("") }
    var transactionAmount by remember { mutableStateOf("") }
    var selectedFromWallet by remember { mutableStateOf(accounts.firstOrNull() ?: AccountEntity(0, "Bank", "Bank", 100000.0)) }
    var selectedToWallet by remember { mutableStateOf(accounts.getOrNull(1) ?: AccountEntity(-1, "E-wallet", "E-wallet", 100000.0)) }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: CategoryEntity(0, "Default", Color.Gray.toArgb())) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showDatePickerModal by remember { mutableStateOf(false) }

    LaunchedEffect(selectedFromWallet) {
        if (selectedToWallet == selectedFromWallet || selectedToWallet.id == -1) {
            selectedToWallet = accounts.minus(selectedFromWallet).first()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (showDatePickerModal) {
            DatePickerModal(
                onDismiss = { showDatePickerModal = false },
                onDateSelected = { date ->
                    transactionDate = date ?: transactionDate
                    showDatePickerModal = false
                }
            )
        }

        val options = mutableMapOf(
            "Pendapatan" to TransactionType.INCOME,
            "Pengeluaran" to TransactionType.EXPENSE,
            "Transfer" to TransactionType.TRANSFER
        )
        TypeOptions(
            options = options,
            onOptionSelected = { transactionType = options[it]!! }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (transactionType != TransactionType.TRANSFER) {
            TransactionForm(
                transactionName = transactionName,
                onTransactionNameChange = { transactionName = it },
                accounts = accounts,
                selectedWallet = selectedWallet,
                onWalletSelected = { selectedWallet = it },
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                transactionDate = transactionDate,
                onDateClick = { showDatePickerModal = true },
                transactionNote = transactionNote,
                onTransactionNoteChange = { transactionNote = it },
                transactionAmount = transactionAmount,
                onTransactionAmountChange = { transactionAmount = it },
                isButtonEnabled = isButtonEnabled,
                onButtonClick = {
                    handleTransactionButtonClick(
                        transactionType = transactionType,
                        selectedWallet = selectedWallet,
                        selectedCategory = selectedCategory,
                        transactionDate = transactionDate,
                        transactionName = transactionName,
                        transactionNote = transactionNote,
                        transactionAmount = transactionAmount,
                        insertTransaction = insertTransaction,
                        onBack = onBack,
                        context = context,
                        isButtonEnabled = isButtonEnabled,
                        setIsButtonEnabled = { isButtonEnabled = it },
                    )
                }
            )
        } else {
            TransferForm(
                accounts = accounts,
                selectedFromWallet = selectedFromWallet,
                onFromWalletSelected = { selectedFromWallet = it },
                selectedToWallet = selectedToWallet,
                onToWalletSelected = { selectedToWallet = it },
                transactionAmount = transactionAmount,
                onTransactionAmountChange = { transactionAmount = it },
                isButtonEnabled = isButtonEnabled,
                onButtonClick = {
                    handleTransferButtonClick(
                        selectedFromWallet = selectedFromWallet,
                        selectedToWallet = selectedToWallet,
                        transactionAmount = transactionAmount,
                        transactionDate = transactionDate,
                        transferFunds = transferFunds,
                        onBack = onBack,
                        context = context,
                        isButtonEnabled = isButtonEnabled,
                        setIsButtonEnabled = { isButtonEnabled = it }
                    )
                }
            )
        }
    }
}

@Composable
fun TypeOptions(
    options: Map<String,TransactionType>,
    onOptionSelected: (String) -> Unit,
) {
    var selectedIndex by remember { mutableStateOf(0) }
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        options.keys.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    selectedIndex = index
                    onOptionSelected(label)
                },
                selected = index == selectedIndex
            ) {
                Text(label)
            }
        }
    }
}

@Composable
fun TransactionForm(
    transactionName: String,
    onTransactionNameChange: (String) -> Unit,
    accounts: List<AccountEntity>,
    selectedWallet: AccountEntity,
    onWalletSelected: (AccountEntity) -> Unit,
    categories: List<CategoryEntity>,
    selectedCategory: CategoryEntity,
    onCategorySelected: (CategoryEntity) -> Unit,
    transactionDate: Long,
    onDateClick: () -> Unit,
    transactionNote: String,
    onTransactionNoteChange: (String) -> Unit,
    transactionAmount: String,
    onTransactionAmountChange: (String) -> Unit,
    isButtonEnabled: Boolean,
    onButtonClick: () -> Unit
) {
    OutlinedTextField(
        value = transactionName,
        onValueChange = onTransactionNameChange,
        label = { Text("Transaction Name") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(text = "Select Wallet:")
    LazyRow {
        items(accounts) { account ->
            AccountItem(
                account = account,
                isSelected = selectedWallet == account,
                onClick = { onWalletSelected(account) }
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCategory.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
                HorizontalDivider()
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = transactionDate.toFormattedDate(),
        onValueChange = {},
        label = { Text("Transaction Date") },
        modifier = Modifier
            .width(200.dp)
            .padding(top = 4.dp)
            .clickable { onDateClick() },
        maxLines = 1,
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledLabelColor = Color.Black,
            disabledBorderColor = Color.Gray,
            disabledLeadingIconColor = Color.Gray,
            disabledTrailingIconColor = Color.Gray,
            disabledPlaceholderColor = Color.Gray
        )
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = transactionNote,
        onValueChange = onTransactionNoteChange,
        label = { Text("Note (Max 150 chars)") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 3
    )

    Spacer(modifier = Modifier.height(8.dp))

    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = transactionAmount,
        onValueChange = onTransactionAmountChange,
        label = { Text("Jumlah") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions (
            onDone = {keyboardController?.hide()}
        )
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onButtonClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isButtonEnabled
    ) {
        Text("Tambahkan Transaksi")
    }
}

@Composable
fun TransferForm(
    accounts: List<AccountEntity>,
    selectedFromWallet: AccountEntity,
    onFromWalletSelected: (AccountEntity) -> Unit,
    selectedToWallet: AccountEntity,
    onToWalletSelected: (AccountEntity) -> Unit,
    transactionAmount: String,
    onTransactionAmountChange: (String) -> Unit,
    isButtonEnabled: Boolean,
    onButtonClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Text(text = "Transfer dari Akun:")
    LazyRow {
        items(accounts) { account ->
            AccountItem(
                account = account,
                isSelected = selectedFromWallet == account,
                onClick = { onFromWalletSelected(account) }
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(text = "Transfer ke Akun:")
    LazyRow {
        items(accounts) { account ->
            if (account != selectedFromWallet) {
                AccountItem(
                    account = account,
                    isSelected = selectedToWallet == account,
                    onClick = { onToWalletSelected(account) }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = transactionAmount,
        onValueChange = onTransactionAmountChange,
        label = { Text("Jumlah yang akan ditransfer") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions (
            onDone = {
                keyboardController?.hide()
            }
        )
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onButtonClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isButtonEnabled
    ) {
        Text("Transfer")
    }
}

fun handleTransactionButtonClick(
    transactionType: TransactionType,
    selectedWallet: AccountEntity,
    selectedCategory: CategoryEntity,
    transactionDate: Long,
    transactionName: String,
    transactionNote: String,
    transactionAmount: String,
    insertTransaction: (TransactionType, String, Int, Int, Long, String, Double) -> Unit,
    onBack: () -> Unit,
    context: Context,
    isButtonEnabled: Boolean,
    setIsButtonEnabled: (Boolean) -> Unit
) {
    Log.d(TAG, "handleTransactionButtonClick: $transactionType, $selectedWallet, $selectedCategory, $transactionDate, $transactionName, $transactionNote, $transactionAmount")
    if (transactionName.isEmpty()||transactionAmount.toDouble()==0.0 || transactionAmount.isEmpty()) {
        setIsButtonEnabled(false)
        Toast.makeText(context, "Nama dan Jumlah tidak boleh kosong", Toast.LENGTH_SHORT).show()
        return
    }else if (transactionAmount.toDouble() > selectedWallet.balance && transactionType == TransactionType.EXPENSE) {
        setIsButtonEnabled(false)
        Toast.makeText(context, "Saldo tidak cukup", Toast.LENGTH_SHORT).show()
        return
    }else if (transactionAmount.isEmpty()||transactionAmount.toDouble()==0.0) {
        setIsButtonEnabled(false)
        Toast.makeText(context, "Jumlah tidak boleh kosong", Toast.LENGTH_SHORT).show()
        return
    }
    if (!isButtonEnabled) {
        setIsButtonEnabled(true)
        if (selectedWallet.id != -1) {
            when (transactionType) {
                TransactionType.INCOME -> {
                    insertTransaction(
                        TransactionType.INCOME,
                        transactionName,
                        selectedWallet.id,
                        selectedCategory.id,
                        transactionDate,
                        transactionNote,
                        transactionAmount.toDouble()
                    )
                    Log.d(TAG, "handleTransactionButtonClick: ${TransactionType.INCOME}, $transactionName, ${selectedWallet.id}, ${selectedCategory.id}, $transactionDate, $transactionNote, ${transactionAmount.toDouble()}")
                }
                TransactionType.EXPENSE -> {
                    insertTransaction(
                        TransactionType.EXPENSE,
                        transactionName,
                        selectedWallet.id,
                        selectedCategory.id,
                        transactionDate,
                        transactionNote,
                        transactionAmount.toDouble()
                    )
                    Log.d(TAG, "handleTransactionButtonClick: ${TransactionType.EXPENSE}, $transactionName, ${selectedWallet.id}, ${selectedCategory.id}, $transactionDate, $transactionNote, ${transactionAmount.toDouble()}")

                }
                else->{

                }
            }
            onBack()
        } else {
            setIsButtonEnabled(false)
            Toast.makeText(context, "Jangan lupa pilih akun", Toast.LENGTH_SHORT).show()
        }
    }
}

fun handleTransferButtonClick(
    selectedFromWallet: AccountEntity,
    selectedToWallet: AccountEntity,
    transactionAmount: String,
    transactionDate: Long,
    transferFunds: (AccountEntity,AccountEntity, Double, Long) -> Unit,
    onBack: () -> Unit,
    context: Context,
    isButtonEnabled: Boolean,
    setIsButtonEnabled: (Boolean) -> Unit
) {
    if (!isButtonEnabled) {
        setIsButtonEnabled(true)
        Log.d(TAG, "handleTransferButtonClick: $selectedFromWallet, $selectedToWallet, $transactionAmount, $transactionDate")
        when {
            selectedFromWallet.id != -1 && selectedToWallet.id != -1 -> {
                transferFunds(
                    selectedFromWallet,
                    selectedToWallet,
                    transactionAmount.toDouble(),
                    transactionDate
                )
                onBack()
            }
            transactionAmount.toDouble() > selectedFromWallet.balance -> {
                setIsButtonEnabled(false)
                Toast.makeText(context, "Saldo tidak cukup", Toast.LENGTH_SHORT).show()
            }
            transactionAmount.isEmpty() -> {
                setIsButtonEnabled(false)
                Toast.makeText(context, "Jumlah tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            transactionAmount.toDouble() == 0.0 -> {
                setIsButtonEnabled(false)
                Toast.makeText(context, "Jumlah tidak boleh 0", Toast.LENGTH_SHORT).show()
            }
            else -> {
                setIsButtonEnabled(false)
                Toast.makeText(context, "Jangan Lupa Pilih Akun", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun AccountItem(
    account: AccountEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(end = 4.dp)
            .height(80.dp)
            .width(160.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(4.dp)
            )
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = account.name,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = CurrencyUtils.formatAmount(account.balance),
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}