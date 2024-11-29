package com.haf.artha.presentation.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.haf.artha.navigation.Screen

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SettingItem(
            title = "Profile"
        )
        SettingItem(
            title = "Category",
            onClick = {
                navController.navigate(Screen.Category.route)
            }
        )
        SettingItem(
            title = "Account"
        )
        SettingItem(
            title = "Transaction"
        )
        SettingItem(
            title = "Overview"
        )
    }
}


@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = {}
) {
    //item that can be clicked to navigate to a new screen
    Column(
        Modifier.fillMaxWidth().padding(vertical = 8.dp)
            .clickable {
                onClick()
            }
    ) {
        Text(title)
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
    }
}

