package com.haf.artha.presentation.onboarding.setCategories

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.haf.artha.presentation.onboarding.component.OnboardingItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetCategory(
    modifier: Modifier = Modifier,
    navController: NavController
) {


    var dummyList =
        mutableListOf(
            "Makan",
            "Transportasi",
            "Hiburan",
            "Belanja",
            "Kesehatan",
            "Pendidikan",
            "Pajak",
            "Insuransi",
            "Tabungan",
            "Investasi",
            "Hadiah",
            "Donasi",
            "Travel",
            "Rumah Tangga",
            "Lainnya")

    var dummies by remember { mutableStateOf(dummyList) }
    val selectedList by remember { mutableStateOf(mutableListOf<String>()) }
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

                repeat(dummies.size) { index ->
                    OnboardingItem(
                        list =dummies,
                        index = index,
                        onClick = {
                            if (selectedList.contains(dummies[index])) {
                                selectedList.remove(dummies[index])
                                Log.d("selectedList", "SetCategory: remove ${dummies[index]}")
                                Log.d("selectedList", "SetCategory: " + selectedList.toString())
                            } else {
                                selectedList.add(dummies[index])
                                Log.d("selectedList", "SetCategory: add ${dummies[index]}")
                                Log.d("selectedList", "SetCategory: " + selectedList.toString())
                            }
                        }
                    )
                }
            }
        }


        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally) {

            var category by remember { mutableStateOf("") }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {newCategory ->
                        category = newCategory
                    },
                    label = { Text("Masukkan Kategori") },
                    modifier = modifier.padding(end = 8.dp)

                )

                Button(
                    onClick = {
                        if (category.isNotEmpty()) {
                            dummies.add(category)
                            Log.d("newcategory", "SetCategory: ${dummies.last()}")
                            Log.d("selectedList", "SetCategory: " + selectedList.toString())
                            category = ""
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "add Icon")
                }
            }



            Button(
                onClick = {
                    if (selectedList.isNotEmpty()) {
                        /*TODO*/
                        // navigate to next screen and add to category db

                        Log.d("selectedList", "SetCategory: " + selectedList.toString())
                    } else {
                        // show error message
                        Log.d("selectedList", "SetCategory: Error ${selectedList.toString()} ")
                    }
                },
                modifier = modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Selesai")
            }
        }
    }


}
