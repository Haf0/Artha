package com.haf.artha.presentation.onboarding.setAccount

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.data.local.entity.AccountEntity
import com.haf.artha.navigation.Screen
import com.haf.artha.preference.PreferenceViewModel
import com.haf.artha.presentation.onboarding.component.OnboardingItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetAccount (
    modifier: Modifier = Modifier,
    navController: NavHostController,
    preferenceViewModel: PreferenceViewModel = hiltViewModel(),
    setAccountViewModel: SetAccountViewModel = hiltViewModel()
) {
    val dummyAccountList = mutableListOf("Tunai", "Bank", "Kartu Kredit", "Kartu Debit", "E-Wallet", "Lainnya")
    val dummies by remember { mutableStateOf(dummyAccountList) }
    val context = LocalContext.current
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
                        preferenceViewModel.setCurrentStep(2)
                        val selectedAccountList = selectedList.map{
                            AccountEntity(type = it, name = it, balance = 0.0)
                        }
                        setAccountViewModel.insertAccount(selectedAccountList)
                        navController.navigate(Screen.SetCategory.route)
                    } else {
                        Toast.makeText(context, "Silahkan Pilih Minimal 1 Akun", Toast.LENGTH_SHORT).show()
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
