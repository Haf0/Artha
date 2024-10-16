package com.haf.artha.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BalanceItem(
    modifier: Modifier = Modifier,
    balance: String
) {

    val style = if (balance.length > 20) {
        MaterialTheme.typography.headlineMedium
    } else {
        MaterialTheme.typography.headlineLarge
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 32.dp, end = 32.dp, bottom = 64.dp)
        ){
            Text(
                text = "Total Saldo",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = modifier
                    .padding(bottom = 16.dp)
            )
            Text(text = balance,
                style = style,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BalanceItemPreview() {
    BalanceItem(
        balance = "Rp 1.000.000.000.000.0"
    )
}