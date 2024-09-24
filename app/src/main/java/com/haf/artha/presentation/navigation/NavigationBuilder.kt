package com.haf.artha.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.haf.artha.presentation.onboarding.setAccount.SetAccount
import com.haf.artha.presentation.onboarding.setCategories.SetCategory
import com.haf.artha.presentation.onboarding.setUsername.SetUsername
import com.haf.artha.presentation.splash.SplashScreen

@Composable
fun NavigationBuilder() {
    val navController = rememberNavController()

    //TODO change the start destination to SplashScreen
    NavHost(
        navController = navController,
        startDestination = Screen.SetUsername.route) {

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

    }
}