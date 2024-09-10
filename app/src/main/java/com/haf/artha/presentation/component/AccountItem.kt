package com.haf.artha.presentation.component

import androidx.compose.material3.FilterChip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun AccountItem(
    modifier: Modifier = Modifier,
    onclick : () -> Unit
) {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        selected = selected,
        onClick = { onclick() },
        label = { /*TODO*/ }
    )
}