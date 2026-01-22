package com.haf.artha.navigation


import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.haf.artha.R

data class BottomNavigationItem(
    val screen: Screen,
    @DrawableRes val icon: Int,
    val label: String
) {
    companion object {
        val items = listOf(
            BottomNavigationItem(Screen.Home , R.drawable.home, "Home"),
            BottomNavigationItem(Screen.Overview, R.drawable.overview, "Overview"),
            BottomNavigationItem(Screen.Account, R.drawable.bank_account, "Wallet"),
            BottomNavigationItem(Screen.Setting, R.drawable.settings, "Setting")
        )
    }
}


@Composable
fun BottomNavigationBar( navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        BottomNavigationItem.items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = item.icon),
                        contentDescription = item.label
                    )
                },
                label = {
                    if(currentRoute == item.screen.route){
                        Text(item.label)
                    }
                },
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(Screen.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true

                    }
                },
                selected = currentRoute == item.screen.route
            )
        }
    }

}