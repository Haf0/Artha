package com.haf.artha.presentation.onboarding.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingItem(
    modifier: Modifier = Modifier,
    list: MutableList<String>,
    listColor: MutableList<Color> = mutableListOf(),
    index: Int,
    onClick: () -> Unit,
    isCategory : Boolean = false

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

            if(isCategory){
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(CircleShape)
                        .size(20.dp)
                        .background(listColor[index])


                )
            }

            Text(
                text = list[index],
                style = MaterialTheme.typography.bodyMedium,
            )
        }


    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview(modifier: Modifier = Modifier) {
    OnboardingItem(
        modifier = modifier,
        list = mutableListOf("Makan", "Transportasi", "Hiburan", "Belanja", "Kesehatan", "Pendidikan", "Pajak", "Insuransi", "Tabungan", "Investasi", "Hadiah", "Donasi", "Travel", "Rumah Tangga", "Lainnya"),
        index = 0,
        onClick = {},
        isCategory = true
    )
}
