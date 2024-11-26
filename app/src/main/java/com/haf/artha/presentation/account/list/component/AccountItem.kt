package com.haf.artha.presentation.account.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.haf.artha.data.local.entity.AccountEntity
import com.haf.artha.navigation.Screen
import com.haf.artha.utils.CurrencyUtils

@Composable
fun AccountItem(
    modifier: Modifier = Modifier,
    account: AccountEntity,
    navController: NavController
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable {
                navController.navigate(Screen.AddAccount.createRoute(accountId = account.id))
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = account.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = CurrencyUtils.formatAmount(account.balance),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
