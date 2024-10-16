package com.haf.artha.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    ){
        Box(
            modifier = modifier.weight(1f)
        ) {
            InOutItem(total = income, isIncome = true)
        }
        Spacer(modifier = modifier.size(16.dp))
        Box(
            modifier = modifier.weight(1f)
        ) {
            InOutItem(total = outcome, isIncome = false)
        }
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
            modifier = modifier.height(120.dp).fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isIncome) {
                Image(modifier = modifier.size(40.dp), imageVector = ImageVector.vectorResource(id = R.drawable.arrowupward), contentDescription = "Income")
            } else {
                Image(modifier = modifier.size(40.dp), imageVector = ImageVector.vectorResource(id = R.drawable.arrowdownward), contentDescription = "Outcome")
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