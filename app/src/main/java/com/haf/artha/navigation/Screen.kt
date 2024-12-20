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
    object DetailTransaction : Screen("transaction/{transactionId}"){
        fun createRoute(transactionId: Int) = "transaction/$transactionId"
    }
    object AddTransaction : Screen("addTransaction")
    object Category : Screen("category")
    object Setting : Screen("setting")
    object About : Screen("about")
    object EditTransaction : Screen("editTransaction")
    object Overview : Screen("overview")
    object SplashScreen : Screen("splashScreen")
}
