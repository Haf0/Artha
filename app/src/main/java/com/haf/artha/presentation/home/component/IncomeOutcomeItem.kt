package com.haf.artha.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haf.artha.R


@Composable
fun IncomeOutcomeItem(
    modifier: Modifier = Modifier,
    income: String,
    outcome: String
) {

    Row(
        modifier = modifier.padding(vertical = 16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        InOutItem(total = income, isIncome = true)
        InOutItem(total = outcome, isIncome = false)
    }
}

@Composable
fun InOutItem(
    modifier: Modifier = Modifier,
    total : String,
    isIncome: Boolean
) {
    Card {
        Row(
            modifier = modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isIncome) {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.arrowupward), contentDescription = "Income")
            } else {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.arrowdownward), contentDescription = "Outcome")
            }
            Column (
                modifier = modifier.padding(8.dp)
            ){

                Text(text = if (isIncome) "Income" else "Outcome")
                Text(text = total, style = MaterialTheme.typography.titleSmall)

            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun preview(modifier: Modifier = Modifier) {
    IncomeOutcomeItem(income = "Rp. 1.0000.0000", outcome = "Rp. -1.0000.0000")
}