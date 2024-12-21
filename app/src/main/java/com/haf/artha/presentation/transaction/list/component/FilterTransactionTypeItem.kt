package com.haf.artha.presentation.transaction.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterTransactionTypeItem(
    name: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) { MaterialTheme.colorScheme.tertiaryContainer } else { MaterialTheme.colorScheme.primaryContainer }
        ),
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)

    ){
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(8.dp)
        )
    }
}