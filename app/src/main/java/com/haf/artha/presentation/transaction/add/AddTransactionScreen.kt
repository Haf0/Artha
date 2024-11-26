@file:OptIn(ExperimentalMaterial3Api::class)

package com.haf.artha.presentation.transaction.add

import DateUtils.convertDateStringToLong
import DateUtils.getTodayDate
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
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.data.local.entity.AccountEntity
import com.haf.artha.data.local.entity.CategoryEntity
import com.haf.artha.data.local.model.TransactionType
import com.haf.artha.presentation.common.UiState
import com.haf.artha.presentation.component.LoadingIndicator
import com.haf.artha.presentation.transaction.add.component.DateInputField
import com.haf.artha.presentation.transaction.add.component.validateDate
import com.haf.artha.utils.CurrencyUtils


@Composable
fun AddTransactionScreen(
    navController: NavHostController,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    viewModel.uiState.collectAsState(UiState.Loading).value.let {
        when(it) {
            is UiState.Success -> {
                val (categories, accounts) = it.data
                AddTransactionContent(
                    categories,
                    accounts,
                    viewModel::addTransaction,
                    viewModel::transferFunds,
                    onBack = {
                        Thread.sleep(2000)
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionContent(
    categories: List<CategoryEntity>,
    accounts : List<AccountEntity>,
    insertTransaction: (TransactionType, String, Int, Int, Long, String, Double) -> Unit,
    transferFunds: (Int, Int, Double, Long) -> Unit,
    onBack: () -> Unit,
    context: Context
) {
    var transactionType by remember { mutableStateOf("Income") }
    var transactionName by remember { mutableStateOf("") }
    var selectedWallet by remember { mutableStateOf(AccountEntity(0,"Bank","Bank", 100000.0)) }

    var transactionDate by remember { mutableStateOf(getTodayDate()) }
    var oldTransactionDate by remember { mutableStateOf(getTodayDate()) }
    var transactionNote by remember { mutableStateOf("") }
    var transactionAmount by remember { mutableStateOf("") }
    var selectedFromWallet by remember { mutableStateOf(AccountEntity(0,"Bank","Bank", 100000.0)) }
    var selectedToWallet by remember { mutableStateOf(AccountEntity(0,"E-wallet","E-wallet", 100000.0))}

    Log.d("categories", "AddTransactionContent: $categories")
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    var isButtonEnabled by remember { mutableStateOf(true) }

    var errorMessage by remember { mutableStateOf("") }

    Column (modifier = Modifier.padding(16.dp)) {
        // Segmented Button
        TypeOptions(
            options = listOf( "Income", "Outcome","Transfer"),
            onOptionSelected = { transactionType = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Income / Outcome Form
        if (transactionType != "Transfer") {
            // Name Input
            OutlinedTextField(
                value = transactionName,
                onValueChange = { transactionName = it },
                label = { Text("Transaction Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Wallet Selection
            Text(text = "Select Wallet:")
            LazyRow {

                items(accounts) { account ->
                    AccountItem(
                        account = account,
                        isSelected = selectedWallet == account,
                        onClick = { selectedWallet = account }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Category Dropdown
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
                                selectedCategory = category
                                expanded = false
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date Input
            DateInputField(
                text= transactionDate,
                onTextChange = {
                        newText ->
                    val cleanInput = newText.replace("/", "")

                    // Handle backspace
                    if (cleanInput.length < oldTransactionDate.replace("/", "").length) {
                        oldTransactionDate = newText
                        transactionDate = newText
                        return@DateInputField
                    }

                    // Format the input if it's valid (<= 8 characters for ddMMyyyy)
                    if (cleanInput.length <= 8) {
                        transactionDate = newText
                        oldTransactionDate = transactionDate
                        errorMessage = validateDate(cleanInput)
                    }
                },
                errorMessage = errorMessage
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Note Input
            OutlinedTextField(
                value = transactionNote,
                onValueChange = {
                    if (it.length <= 150) transactionNote = it
                },
                label = { Text("Note (Max 150 chars)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Amount Input
            OutlinedTextField(
                value = transactionAmount,
                onValueChange = {
                    if (it.all { char -> char.isDigit() } && it != "0") {
                        transactionAmount = it
                    }
                },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add Button
            Button(
                onClick = {
                    Log.d("insertwalletid", "AddTransactionContent: $selectedWallet")
                    if(isButtonEnabled){

                        if (selectedWallet.id != 0){
                            Log.d("insert", "AddTransactionContent: $transactionType")
                            when(transactionType){
                                "Income" -> insertTransaction(
                                    TransactionType.INCOME,
                                    transactionName,
                                    selectedWallet.id,
                                    selectedCategory.id,
                                    convertDateStringToLong(transactionDate),
                                    transactionNote,
                                    transactionAmount.toDouble()
                                )
                                "Outcome" -> insertTransaction(
                                    TransactionType.EXPENSE,
                                    transactionName,
                                    selectedWallet.id,
                                    selectedCategory.id,
                                    convertDateStringToLong(transactionDate),
                                    transactionNote,
                                    transactionAmount.toDouble()
                                )
                            }
                            onBack()
                        }else{
                            Toast.makeText(context, "Jangan lupa pilih akun", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Transaction")
            }

        } else {
            // Transfer Form
            Text(text = "Transfer From:")
            LazyRow {
                items(accounts) { account ->
                    if (account != selectedToWallet) {
                        AccountItem(
                            account = account,
                            isSelected = selectedFromWallet == account,
                            onClick = { selectedFromWallet = account }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Transfer To:")
            LazyRow {
                items(accounts) { account ->
                    AccountItem(
                        account = account,
                        isSelected = selectedToWallet == account,
                        onClick = { selectedToWallet = account }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Amount Input for Transfer
            OutlinedTextField(
                value = transactionAmount,
                onValueChange = {
                    if (it.all { char -> char.isDigit() } && it != "0") {
                        transactionAmount = it
                    }
                },
                label = { Text("Transfer Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Transfer Button
            Button(
                onClick = {
                    if(isButtonEnabled){
                        if(selectedFromWallet.id != 0 || selectedToWallet.id != 0){
                            transferFunds(
                                selectedFromWallet.id,
                                selectedToWallet.id,
                                transactionAmount.toDouble(),
                                convertDateStringToLong(transactionDate)
                            )
                            onBack()
                        }else{
                            Toast.makeText(context, "Jangan lupa pilih akun", Toast.LENGTH_SHORT).show()
                        }

                    }


                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Transfer")
            }
        }
    }
}

// Helper function for Segmented Button
@Composable
fun TypeOptions(
    options: List<String>,
    onOptionSelected: (String) -> Unit,
) {
    var selectedIndex by remember{ mutableStateOf(0) }
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, label ->
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

// ChipItem Composable
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

