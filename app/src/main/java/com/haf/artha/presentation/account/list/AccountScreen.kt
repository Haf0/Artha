package com.haf.artha.presentation.account.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.data.local.entity.AccountEntity
import com.haf.artha.navigation.Screen
import com.haf.artha.presentation.account.list.component.AccountItem
import com.haf.artha.presentation.common.UiState
import com.haf.artha.presentation.component.LoadingIndicator

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AccountScreenViewModel = hiltViewModel()
) {
    val accountList by viewModel.accountList.collectAsState()

    when(accountList){
        is UiState.Loading -> {
            LoadingIndicator()
        }
        is UiState.Error -> {
            // Log.d("homeScreen", "HomeScreen: loading")
        }
        is UiState.Success -> {
            AccountScreenContent(
                modifier = modifier,
                navController = navController,
                accountList = (accountList as UiState.Success).data
            )
        }
    }

}

@Composable
fun AccountScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    accountList: List<AccountEntity>
) {
    Box(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(accountList) { account ->
                AccountItem(
                    account = account,
                    navController = navController
                )
            }
        }

        Button(
            onClick = {
                navController.navigate(Screen.AddAccount.createRoute())
            },
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
        ) {
            Text("Tambah Akun")
        }
    }


}