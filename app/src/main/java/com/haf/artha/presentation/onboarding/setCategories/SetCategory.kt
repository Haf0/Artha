package com.haf.artha.presentation.onboarding.setCategories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetCategory(
    modifier: Modifier = Modifier
) {
    val selected = remember { mutableStateOf(false) }

    val dummyList =
        listOf("Food",
            "Transport",
            "Entertainment",
            "Shopping",
            "Health",
            "Education",
            "Utilities",
            "Insurance",
            "Savings",
            "Investments",
            "Gifts",
            "Donations",
            "Travel",
            "Personal Care",
            "Housing",
            "Miscellaneous")
    val selectedList = mutableListOf<String>()
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pilih Kategori",
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(8.dp)
            )
            Text(
                text = "Pilih kategori yang ingin kamu tambahkan",
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(8.dp)
            )
            FlowRow(
                modifier = modifier.fillMaxSize()
            ) {
                repeat(dummyList.size) { index ->
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
                                if (selected.value) {
                                    selectedList.add(dummyList[index])
                                } else {
                                    selectedList.remove(dummyList[index])
                                }
                            },
                        elevation = CardDefaults.cardElevation(4.dp)

                    ) {

                        Text(
                            text = dummyList[index],
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )

                    }
                }
            }
        }


        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Button(
                onClick = {

                },
                modifier = modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Lanjutkan")
            }
        }
    }


}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun preview(modifier: Modifier = Modifier) {
    SetCategory()
}

