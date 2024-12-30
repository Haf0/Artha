package com.haf.artha.presentation.overview

import DateUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun OverviewScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    OverViewContent()
}


@Composable
fun OverViewContent(modifier: Modifier = Modifier) {
    val time = DateUtils.getTodayMonthAndYear()
    var years by remember { mutableIntStateOf(time.first) }
    var months by remember { mutableIntStateOf(time.second) }

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
    }

}

@Composable
fun PieChartContent(
    modifier: Modifier = Modifier
) {

}