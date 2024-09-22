package com.haf.artha.presentation.onboarding.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingItem(
    modifier: Modifier = Modifier,
    list: MutableList<String>,
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

        Text(
            text = list[index],
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )

    }
}
