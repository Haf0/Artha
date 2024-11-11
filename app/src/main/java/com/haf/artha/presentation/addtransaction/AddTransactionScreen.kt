@file:OptIn(ExperimentalMaterial3Api::class)

package com.haf.artha.presentation.addtransaction

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.haf.artha.data.local.entity.AccountEntity
import com.haf.artha.presentation.addtransaction.component.DateInputField
import com.haf.artha.presentation.addtransaction.component.formatDate
import com.haf.artha.presentation.addtransaction.component.validateDate
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddTransactionScreen(
    navController: NavHostController
) {
    AddTransactionContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionContent() {
    var transactionType by remember { mutableStateOf("Income") }
    var transactionName by remember { mutableStateOf("") }
    var selectedWallet by remember { mutableStateOf(AccountEntity(0,"Bank","Bank", 100000.0)) }
    var selectedCategory by remember { mutableStateOf("Food") }
    var transactionDate by remember { mutableStateOf(getTodayDate()) }
    var oldTransactionDate by remember { mutableStateOf(getTodayDate()) }
    var transactionNote by remember { mutableStateOf("") }
    var transactionAmount by remember { mutableStateOf("") }

    var selectedFromWallet by remember { mutableStateOf(AccountEntity(0,"Bank","Bank", 100000.0)) }
    var selectedToWallet by remember { mutableStateOf(AccountEntity(0,"E-wallet","E-wallet", 100000.0))}

    val wallets = listOf(AccountEntity(0,"Bank","Bank", 100000.0),AccountEntity(0,"E-wallet","E-wallet", 100000.0), AccountEntity(0,"CC","CC", 100000.0),AccountEntity(0,"E-wallet","E-wallet", 100000.0), AccountEntity(0,"CC","CC", 100000.0))
    val categories = listOf("Food", "Shopping", "Salary", "Transport")



    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf<String?>(null) }

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
            TextField(
                value = transactionName,
                onValueChange = { transactionName = it },
                label = { Text("Transaction Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Wallet Selection
            Text(text = "Select Wallet:")
            LazyRow {
                items(wallets) { wallet ->
                    AccountItem(
                        account = wallet,
                        isSelected = selectedWallet == wallet,
                        onClick = { selectedWallet = wallet }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Category Dropdown
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date Input
            TextField(
                value = transactionDate,
                onValueChange = { transactionDate = it },
                label = { Text("Date (DD/MM/YYYY)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Note Input
            TextField(
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
            TextField(
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
                onClick = { /* Handle Add Transaction */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Transaction")
            }

        } else {
            // Transfer Form
            Text(text = "Transfer From:")
            LazyRow {
                items(wallets) { wallet ->
                    if (wallet != selectedToWallet) {
                        AccountItem(
                            account = wallet,
                            isSelected = selectedFromWallet == wallet,
                            onClick = { selectedFromWallet = wallet }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Transfer To:")
            LazyRow {
                items(wallets) { wallet ->
                    AccountItem(
                        account = wallet,
                        isSelected = selectedToWallet == wallet,
                        onClick = { selectedToWallet = wallet }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Amount Input for Transfer
            TextField(
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
                        transactionDate = formatDate(cleanInput)
                        oldTransactionDate = transactionDate
                        errorMessage = validateDate(cleanInput)
                    }
                },
                errorMessage = errorMessage
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Transfer Button
            Button(
                onClick = { /* Handle Transfer */ },
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
            .height(40.dp)
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
                text = formatAmount(account.balance),
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}



private fun formatAmount(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount)
}

// Utility function for getting today's date
fun getTodayDate(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date())
}


@Preview
@Composable
fun preview(){
    AddTransactionContent()
}