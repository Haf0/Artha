package com.haf.artha

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.haf.artha.presentation.home.HomeScreen
import com.haf.artha.presentation.navigation.BottomNavigationBar
import com.haf.artha.presentation.navigation.BottomNavigationItem
import com.haf.artha.presentation.navigation.Screen
import com.haf.artha.presentation.onboarding.setAccount.SetAccount
import com.haf.artha.presentation.onboarding.setCategories.SetCategory
import com.haf.artha.presentation.onboarding.setUsername.SetUsername
import com.haf.artha.presentation.overview.OverviewScreen
import com.haf.artha.presentation.splash.SplashScreen

@Composable
fun ArthaApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route.toString()
    Log.d("route", "route: $currentRoute")
    val showBottomBar = currentRoute in BottomNavigationItem.items.map { it.screen.route }
    Log.d("btt", "ArthaApp: $showBottomBar")
    Scaffold(
        bottomBar = {
            if (showBottomBar){
                BottomNavigationBar(
                    navController = navController
                )
            }

        }
    ) { innerPadding ->

        //change StartDestination to SplashScreen
        NavHost(
            navController = navController,
            startDestination = Screen.SetUsername.route,
            modifier = Modifier.padding(
                if (showBottomBar) innerPadding else PaddingValues(
                    top = 0.dp,
                    start = 0.dp,
                    end = 0.dp,
                    bottom = 0.dp
                )
            )
        ) {

            composable(Screen.SplashScreen.route) {
                SplashScreen(navController = navController)
            }

            composable(Screen.SetUsername.route) {
                SetUsername(navController = navController)
            }

            composable(Screen.SetCategory.route) {
                SetCategory(navController = navController)
            }

            composable(Screen.SetAccount.route) {
                SetAccount(navController = navController)
            }

            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }

            composable(Screen.Overview.route){
                OverviewScreen(navController = navController)
            }

        }
    }
}