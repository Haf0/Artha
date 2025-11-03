package com.haf.artha.presentation.overview

import DateUtils
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import java.time.Month

@SuppressLint("NewApi")
@Composable
fun OverviewScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    OverViewContent()
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OverViewContent(modifier: Modifier = Modifier) {
    val time = DateUtils.getTodayMonthAndYear()
    var years by remember { mutableIntStateOf(time.first) }
    var months by remember { mutableIntStateOf(time.second) }

    val context = LocalContext.current
    //val bitmap = createIncomeExpenseBitmap(context,true, "+20%", "-10%")

    Column {
        Text(text = "$months $years")
        Button(
            onClick = {
                when (months) {
                    12 -> {
                        years += 1
                        months = 1
                    }
                    else -> {
                        months += 1
                    }
                }
            }
        ) {
            Text(text = "Next")
        }
        Button(
            onClick = {
                when (months) {
                    1 -> {
                        years -= 1
                        months = 12
                    }
                    else -> {
                        months -= 1
                    }
                }
            }
        ) {
            Text(text = "Previous")
        }

        val monthName = Month.of(months).name
        Button(
            onClick = {
                //shareBitmap(context = context, bitmap = bitmap, title= "$monthName $years")
            }
        ) {
            Icon(
                Icons.Default.Share,
                contentDescription = "Share"
            )
        }
    }

}

@Composable
fun PieChartContent(
    modifier: Modifier = Modifier
) {

}