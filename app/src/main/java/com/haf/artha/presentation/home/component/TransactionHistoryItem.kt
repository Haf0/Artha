package com.haf.artha.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haf.artha.R
import com.haf.artha.data.local.model.TransactionType

@Composable
fun TransactionHistoryItem(
    modifier: Modifier = Modifier,
    title: String,
    date: String,
    amount: String,
    transactionType: TransactionType,
    onClickItem : () -> Unit
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clickable { onClickItem() }
    ) {
        Row(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = modifier.padding(end = 16.dp).size(40.dp),
                imageVector = when(transactionType){
                    TransactionType.INCOME -> ImageVector.vectorResource(id = R.drawable.arrowdownward)
                    TransactionType.EXPENSE -> ImageVector.vectorResource(id = R.drawable.arrowupward)
                    else -> {
                        ImageVector.vectorResource(id = R.drawable.transfericon)
                    }
                },
                contentDescription = "transactionType"
            )
            Column(
                modifier = modifier.fillMaxHeight()
            ) {
                Text(text = title,modifier.width(150.dp) , maxLines = 1 , overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Medium, color = Color.Black)
                //Text(text = date, modifier = modifier.padding(top = 4.dp), fontSize = 14.sp)
            }
            Spacer(Modifier.weight(1f).fillMaxHeight())
            Text(text = amount, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}