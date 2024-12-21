package com.haf.artha.presentation.transaction.list.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CurrencyOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label : String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            val formattedValue = newValue.filter { it.isDigit() }
            onValueChange(formattedValue)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        visualTransformation = CurrencyVisualTransformation(),
        modifier = modifier,
        maxLines = 1
    )
}

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedText = formatNumber(originalText)
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (originalText.isEmpty()) return 0
                var transformedOffset = 0
                var originalOffset = 0
                while (originalOffset < offset && transformedOffset < formattedText.length) {
                    if (formattedText[transformedOffset].isDigit() || formattedText[transformedOffset] == ',') {
                        originalOffset++
                    }
                    transformedOffset++
                }
                return transformedOffset.coerceAtMost(formattedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (originalText.isEmpty()) return 0
                var originalOffset = 0
                var transformedOffset = 0
                while (transformedOffset < offset && transformedOffset < formattedText.length) {
                    if (formattedText[transformedOffset].isDigit() || formattedText[transformedOffset] == ',') {
                        originalOffset++
                    }
                    transformedOffset++
                }
                return originalOffset.coerceAtMost(originalText.length)
            }

        }


        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }

    private fun formatNumber(number:String):String{
        val amount = number.toDoubleOrNull() ?: 0.0
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 0
        return formatter.format(amount)
    }
}