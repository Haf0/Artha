package com.haf.artha.presentation.overview.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

data class PieChartData(
    val color: Color,
    val value: Int,
    val description: String
)

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    dataPoints: List<PieChartData>
) {
    var pieCenter by remember { mutableStateOf(Offset.Zero) }

    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }

    val textMeasurer = rememberTextMeasurer()
    val totalValue = dataPoints.sumOf { it.value }
    val totalDegrees = 360f

    Canvas(
        modifier = modifier
            .size(300.dp)
    ) {
        val width = size.width
        val height = size.height
        val radius = width / 2f

        pieCenter = Offset(x = width / 2f, y = height / 2f)
        var currentStartAngle = 0f

        dataPoints.forEach { pieChartData ->
            val sweepAngle = (pieChartData.value.toFloat() / totalValue) * totalDegrees
            val angleToDraw = sweepAngle * animationProgress.value

            // Draw slice
            scale(0.95f) {
                drawArc(
                    color = pieChartData.color,
                    startAngle = currentStartAngle,
                    sweepAngle = angleToDraw,
                    useCenter = true,
                    size = Size(
                        width = radius * 2f,
                        height = radius * 2f
                    ),
                    topLeft = Offset(
                        (width - radius * 2f) / 2f,
                        (height - radius * 2f) / 2f
                    )
                )
            }

            val percentage = (pieChartData.value / totalValue.toFloat() * 100).toInt()

            if (animationProgress.value > 0.5f && percentage > 5) {
                val midAngle = currentStartAngle + (angleToDraw / 2f)
                val radianAngle = Math.toRadians(midAngle.toDouble())

                val textRadius = radius * 0.7f
                val textX = (cos(radianAngle) * textRadius + pieCenter.x).toFloat()
                val textY = (sin(radianAngle) * textRadius + pieCenter.y).toFloat()

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "$percentage%",
                        textX,
                        textY,
                        android.graphics.Paint().apply {
                            textSize = 14.sp.toPx()
                            textAlign = android.graphics.Paint.Align.CENTER
                            color = Color.White.toArgb()
                            isFakeBoldText = true
                        }
                    )
                }
            }

            currentStartAngle += angleToDraw
        }
    }
}
