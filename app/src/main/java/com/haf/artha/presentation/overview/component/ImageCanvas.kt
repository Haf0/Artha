package com.haf.artha.presentation.overview.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haf.artha.R

@Composable
fun SharedImage(modifier: Modifier = Modifier, isProfit: Boolean) {
    val context = LocalContext.current
    Column {
        val bitmap = createIncomeExpenseBitmap(context,"November 2025", isProfit, "+20%")
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Income Expense Graph",
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

fun createIncomeExpenseBitmap(
    context: Context,
    date: String,
    isProfit: Boolean,
    nettProfit : String,
): Bitmap {
    val drawableResId = if(isProfit) R.drawable.profit else R.drawable.loss

    val imageBitmap = BitmapFactory.decodeResource(context.resources, drawableResId)
    val width = imageBitmap.width
    val height = imageBitmap.height
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    val density = context.resources.displayMetrics.density
    val topMarginPx = 16.dp.value * density

    canvas.drawBitmap(imageBitmap, 0f, 0f, null) // Draw as background

    //Header stuff
    val headerPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 145f
        isFakeBoldText = true
        textAlign = android.graphics.Paint.Align.CENTER
    }

    val headerCenterX = width / 2f
    val headerFontMetrics = headerPaint.fontMetrics
    val headerTargetY = topMarginPx
    val headerBaselineY = headerTargetY - headerFontMetrics.ascent

    canvas.drawText(date, headerCenterX, headerBaselineY, headerPaint)

    // Nett Profit/Loss stuff
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 60f
        isFakeBoldText = true
    }

    val yNettLabelText = if(isProfit) height*0.75f else height*0.30f
    val yNettPercentageText = if(isProfit) height*0.82f else height*0.37f

    val nettLabel = if(isProfit) "Pendapatan Bersih:" else "Kerugian Bersih:"
    canvas.drawText(nettLabel, width*0.6f, yNettLabelText, paint)
    canvas.drawText(nettProfit, width*0.6f, yNettPercentageText, paint)

    return bitmap
}

@Preview(showBackground = true)
@Composable
fun GraphPreview() {
    SharedImage(isProfit = true)
}

@Preview(showBackground = true)
@Composable
private fun LossPreview() {
    SharedImage(isProfit = false)
    
}