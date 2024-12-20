package com.haf.artha.presentation.transaction.list.component


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.haf.artha.data.local.entity.CategoryEntity

@Composable
fun FilterCategpryItem(
    modifier: Modifier = Modifier,
    list: MutableList<CategoryEntity>,
    index: Int,
    onClick: () -> Unit

) {
    val selected = remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected.value) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        ),
        modifier = modifier
            .padding(4.dp)
            .clickable {
                selected.value = !selected.value
                onClick()
            },
        elevation = CardDefaults.cardElevation(4.dp)

    ) {

        Row(
            modifier = modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(CircleShape)
                    .size(20.dp)
                    .background(Color(list[index].color))
            )
            Text(
                text = list[index].name,
                style = MaterialTheme.typography.bodyMedium,
            )
        }


    }
}

