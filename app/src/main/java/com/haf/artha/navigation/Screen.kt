package com.haf.artha.navigation

sealed class Screen(
    val route: String
) {
    object SetCategory : Screen("setCategory")
    object SetAccount : Screen("setAccount")
    object SetUsername : Screen("setUsername")
    object Home : Screen("home")

    object Account : Screen("account")
    object AddAccount : Screen("addAccount/accountId={accountId}"){
        fun createRoute(accountId: Int? = null) = "addAccount/accountId=$accountId"
    }

    object Transaction : Screen("transaction")
    object AddTransaction : Screen("addTransaction")

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
