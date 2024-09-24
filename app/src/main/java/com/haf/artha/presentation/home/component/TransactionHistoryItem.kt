package com.haf.artha.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TransactionHistoryItem(
    modifier: Modifier = Modifier,
    intColor: Int,
    title: String,
    date: String,
    amount: String
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box (
                modifier = modifier
                    .padding(end = 8.dp)
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(Color(intColor))

            )

            Column(
                modifier = modifier.fillMaxHeight()
            ) {
                Text(text = title,modifier.width(150.dp) , maxLines = 1 , overflow = TextOverflow.Ellipsis)
                Text(text = date, modifier = modifier.padding(top = 8.dp))
            }
            Spacer(Modifier.weight(1f).fillMaxHeight())
            Text(text = amount)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Previw(modifier: Modifier = Modifier) {
    data class dummmmy(
        val color: Int,
        val title: String,
        val date: String,
        val amount: String
    )
    val listDummy : List<dummmmy> = listOf(
        dummmmy(color = 1584152, title = "Transaction 1", date = "2023-01-01", amount = "$100"),
        dummmmy(color = 0x00FF00, title = "Transactiasdasdasdasdasdasdaasdasdasdasdasdon 2", date = "2023-01-02", amount = "Rp100.000.000"),
        dummmmy(color = 0x0000FF, title = "Transaction 3", date = "2023-01-03", amount = "$3000000")
    )
    LazyColumn {
        items(listDummy){
            TransactionHistoryItem(
                intColor = it.color,
                title = it.title,
                date = it.date,
                amount = it.amount
            )
        }
    }
}