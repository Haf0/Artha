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
fun randoma(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column {
        val bitmap = createIncomeExpenseBitmap(context, "+20%", "-10%")
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
    incomeValue: String,
    expenseValue: String
): Bitmap {
    val imageBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.profit)
    val width = imageBitmap.width
    val height = imageBitmap.height
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

    canvas.drawBitmap(imageBitmap, 0f, 0f, null) // Draw as background

    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 145f
        isFakeBoldText = true
    }
    canvas.drawText("Income  : $incomeValue", width*0.6f, height*0.75f, paint)
    canvas.drawText("Expense : $expenseValue", width*0.6f, height*0.82f, paint)

    return bitmap
}

@Preview(showBackground = true)
@Composable
fun GraphPreview() {
    randoma()
}