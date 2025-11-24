package com.haf.artha.presentation.overview

import DateUtils
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.presentation.overview.component.ExpenseIncomePieChart
import com.haf.artha.presentation.overview.component.PieChart
import com.haf.artha.presentation.overview.component.PieChartLegend
import java.time.Month

@SuppressLint("NewApi")
@Composable
fun OverviewScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Column(
        modifier .fillMaxSize()
            .padding(16.dp)
    ) {
        OverViewContent()
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OverViewContent(
    modifier: Modifier = Modifier,
    viewModel: OverviewViewModel = hiltViewModel()
) {
    val time = DateUtils.getTodayMonthAndYear()
    var years by remember { mutableIntStateOf(time.first) }
    var months by remember { mutableIntStateOf(time.second+1) }
    val monthName = Month.of(months).name
    viewModel.getCategoriesAmount(years, months)
    viewModel.getIncomeAmount(years, months)
    viewModel.getExpenseAmount(years, months)
    val pieChartData by viewModel.categoryData.collectAsState()
    val incomeAmount by viewModel.incomeData.collectAsState()
    val expenseAmount by viewModel.expenseData.collectAsState()

    val context = LocalContext.current
    //val bitmap = createIncomeExpenseBitmap(context,true, "+20%", "-10%")

    Column(
        modifier.verticalScroll(rememberScrollState())
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                Text(
                    text = "<",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                )
            }
            Text(
                text = "$monthName $years",
                modifier.padding(horizontal = 5.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            )
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
                Text(
                    text = ">",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            PieChart(data = pieChartData)
        }
        Spacer(modifier = Modifier.height(16.dp))
        PieChartLegend(data = pieChartData)

        ExpenseIncomePieChart(
            expenseData = expenseAmount,
            incomeData = incomeAmount,
            modifier = Modifier
                .padding(top = 16.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    //shareBitmap(context = context, bitmap = bitmap, title= "$monthName $years")
                }
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Share",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Bagikan")
            }
        }
    }

}
