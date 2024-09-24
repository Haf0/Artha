package com.haf.artha.presentation.onboarding.setAccount

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.haf.artha.presentation.navigation.Screen
import com.haf.artha.presentation.onboarding.component.OnboardingItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetAccount (
    modifier: Modifier = Modifier,
    navController: NavController
) {
    //tommorow add something like category but for account and list all local e wallet

    val dummyAccountList = mutableListOf("Cash", "Bank", "Credit Card", "Debit Card", "E-Wallet", "Others")
    val dummies by remember { mutableStateOf(dummyAccountList) }
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
                text = "Pilih Akun",
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(8.dp)
            )
            Text(
                text = "Pilih akun yang ingin kamu tambahkan",
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
                            } else {
                                selectedList.add(dummies[index])
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

            Button(
                onClick = {
                    if (selectedList.isNotEmpty()) {
                        /*TODO*/
                        // navigate to next screen and add to category db

                        navController.navigate(Screen.SetCategory.route)
                        Log.d("selectedList", "SetCategory: " + selectedList.toString())
                    } else {
                        // show error message
                    }
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
