package com.haf.artha.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AccountItem(
    modifier: Modifier = Modifier,
    onclick : () -> Unit
) {
    //tommorow add something like category but for account and list all local e wallet

    val listEwalletIndonesia = listOf("OVO", "DANA", "LinkAja", "GoPay", "ShopeePay", "PayPal", "Sakuku", "Jenius", "BCA", "BNI", "Mandiri", "BRI", "CIMB Niaga", "Permata", "Maybank", "Panin", "Danamon", "BTPN", "Bukalapak", "Tokopedia", "Blibli", "Alfamart", "Indomaret", "Circle K", "7 Eleven", "Lawson", "FamilyMart", "Hypermart", "Carrefour", "Giant",
}