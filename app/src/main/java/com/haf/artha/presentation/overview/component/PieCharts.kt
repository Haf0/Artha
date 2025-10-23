package com.haf.artha.presentation.overview.component

import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChartContent(
    data: List<CategoryAmountq>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Text("No data")
        return
    }

    val total = data.sumOf { it.amount }
    var startAngle = 0f

    Canvas(modifier = modifier.size(200.dp)) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)
        data.forEach { item ->
            val sweep = (item.amount / total * 360f).toFloat()
            drawArc(
                color = Color(item.color),
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true,
                size = Size(size.width, size.height)
            )
            val percent = item.amount / total * 100
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
fun PieChartLegend(data: List<CategoryAmountq>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        data.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color(item.color))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.categoryName)
            }
        }
    }
}

data class CategoryAmountq(
    val categoryName: String,
    val amount: Double,
    val color: Int
)

@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
    val sampleData = listOf(
        CategoryAmountq("Food", 300.0, 0xFFE57373.toInt()),
        CategoryAmountq("Transport", 150.0, 0xFF64B5F6.toInt()),
        CategoryAmountq("Shopping", 200.0, 0xFF81C784.toInt()),
        CategoryAmountq("Income", 500.0, 0xFFFFD54F.toInt()),
        CategoryAmountq("Health", 120.0, 0xFFBA68C8.toInt()),
        CategoryAmountq("Entertainment", 180.0, 0xFFFF8A65.toInt()),
        CategoryAmountq("Utilities", 220.0, 0xFF4DD0E1.toInt()),
        CategoryAmountq("Education", 90.0, 0xFFAED581.toInt()),
        CategoryAmountq("Travel", 250.0, 0xFFDCE775.toInt()),
        CategoryAmountq("Gifts", 200.0, 0xFFFFB74D.toInt())
    )
    Column {
        PieChartContent(data = sampleData)
        Spacer(modifier = Modifier.height(16.dp))
        PieChartLegend(data = sampleData)
    }
}