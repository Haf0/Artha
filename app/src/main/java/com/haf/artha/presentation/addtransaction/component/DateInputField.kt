package com.haf.artha.presentation.addtransaction.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DateInputField(
    text: String,
    onTextChange: (String) -> Unit,
    errorMessage: String
) {

    /*TODO Still error*/
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text("Enter Date (dd/MM/yyyy)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 18.sp),
        isError = errorMessage.isNotEmpty(),
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = DateVisualTransformation()
    )

    if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

fun formatDate(input: String): String {
    val sb = StringBuilder()
    for (i in input.indices) {
        sb.append(input[i])
        if (i == 1 || i == 3) {
            sb.append("/")
        }
    }
    return sb.toString()
}

class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 10) text.text.substring(0, 10) else text.text
        val formatted = buildString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if (i == 1 || i == 3) append('/')
            }
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 1 -> offset
                    offset <= 3 -> offset + 1
                    offset <= 10 -> offset + 2
                    else -> offset + 2
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 5 -> offset - 1
                    offset <= 12 -> offset - 2
                    else -> offset - 2
                }
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

fun validateDate(input: String): String {
    if (input.length < 8) return ""
    val day = input.substring(0, 2).toIntOrNull() ?: return "Invalid day"
    val month = input.substring(2, 4).toIntOrNull() ?: return "Invalid month"
    val year = input.substring(4).toIntOrNull() ?: return "Invalid year"
    if (day !in 1..31) return "Day must be between 01 and 31"
    if (month !in 1..12) return "Month must be between 01 and 12"
    if (year !in 1900..2100) return "Year must be between 1900 and 2100"
    return ""
}

fun convertDateToLong(date: String): Long? {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.parse(date)?.time
    } catch (e: Exception) {
        null
    }
}

fun saveDateToDatabase(dateLong: Long) {
    // Simulate saving to a database (this is just a placeholder for a real database operation)
    println("Date saved to database as timestamp: $dateLong")
}