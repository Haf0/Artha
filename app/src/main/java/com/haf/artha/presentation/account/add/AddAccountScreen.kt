package com.haf.artha.presentation.account.add

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun AddAccountScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    accountId: Int? = null
) {
    val context = LocalContext.current
    AddAccountScreenContent(
        modifier = modifier,
        navController = navController,
        context = context,
        accountId = accountId
    )
}

@Composable
fun AddAccountScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context,
    accountId: Int? = null,
    viewModel: AddAccountScreenViewModel = hiltViewModel()
) {
    var accountName by remember { mutableStateOf("") }
    var accountBalance by remember { mutableStateOf("0") }

    LaunchedEffect(accountId) {
        if (accountId != null){
            val account = viewModel.getAccount(accountId)
            account.let {
                if (it != null) {
                    accountName = it.name
                    accountBalance = it.balance.toInt().toString()
                }
            }

        }

    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = accountName,
                onValueChange = { accountName = it },
                label = { Text("Nama Akun") },
                modifier = modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = accountBalance,
                onValueChange = { accountBalance = it },
                label = { Text("Saldo Awal") },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
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
                    if (accountName.isEmpty() || accountBalance.toDouble() == 0.0){
                        Toast.makeText(context, "Tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                    }else{
                        if (accountId != null){
                            viewModel.updateAccount(accountId, accountName, accountBalance.toDouble())
                        }else{
                            viewModel.addAccount(accountName, accountBalance.toDouble())
                        }
                    }
                    navController.popBackStack()
                }
            ) {
                Text(if(accountId == null)"Tambahkan Akun" else "Ubah Akun")
            }
        }
    }

}