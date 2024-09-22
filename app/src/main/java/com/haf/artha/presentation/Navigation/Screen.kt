package com.haf.artha.presentation.Navigation

sealed class Screen(
    val route: String
) {
    object Onboarding : Screen("onboarding")
    object SetCategory : Screen("setCategory")
    object SetAccount : Screen("setAccount")
    object SetUsername : Screen("setUsername")
    object Home : Screen("home")
    object Account : Screen("account")
    object Transaction : Screen("transaction")
    object AddTransaction : Screen("addTransaction")
    object AddAccount : Screen("addAccount")
    object AddCategory : Screen("addCategory")
    object Setting : Screen("setting")
    object About : Screen("about")
    object Notification : Screen("notification")
    object EditAccount : Screen("editAccount")
    object EditCategory : Screen("editCategory")
    object EditTransaction : Screen("editTransaction")
    object Overview : Screen("overview")
    object SplashScreen : Screen("splashScreen")
}
