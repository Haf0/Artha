package com.haf.artha.presentation.overview.component

import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.haf.artha.data.local.model.CategoryAmount
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(
    data: List<CategoryAmount>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Text("TIDAK ADA DATA")
        return
    }

    val total = data.sumOf { it.totalAmount }
    var startAngle = 0f

    Canvas(modifier = modifier.size(200.dp)) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)
        data.forEach { item ->
            val sweep = (item.totalAmount / total * 360f).toFloat()
            drawArc(
                color = Color(item.categoryColor),
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true,
                size = Size(size.width, size.height)
            )
            val percent = item.totalAmount / total * 100
            val angle = startAngle + sweep / 2
            val radian = Math.toRadians(angle.toDouble())
            val textRadius = radius * 0.7f
            val x = center.x + (textRadius * cos(radian)).toFloat()
            val y = center.y + (textRadius * sin(radian)).toFloat()

            drawContext.canvas.nativeCanvas.apply {
                val paint = TextPaint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 32f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                drawText(
                    "${percent.toInt()}%",
                    x,
                    y,
                    paint
                )
            }
            startAngle += sweep
        }
    }
}
@Composable
fun ExpenseIncomePieChart(
    expenseData: Double,
    incomeData: Double,
    modifier: Modifier = Modifier
){
    val data = if(expenseData!=0.0 || incomeData != 0.0) listOf(
        CategoryAmount(
            categoryId = 0,
            categoryName = "Pengeluaran",
            categoryColor = 0xFFE57373.toInt(),
            year = "",
            month = "",
            totalAmount = expenseData
        ),
        CategoryAmount(
            categoryId = 1,
            categoryName = "Pendapatan",
            categoryColor = 0xFF81C784.toInt(),
            year = "",
            month = "",
            totalAmount = incomeData
        )
    ) else emptyList()
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        PieChart(data = data, modifier = modifier)
    }
    Spacer(modifier = Modifier.size(16.dp))
    PieChartLegend(data)
}
@Composable
fun PieChartLegend(data: List<CategoryAmount>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        data.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color(item.categoryColor))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.categoryName)
            }
        }
    }
}
