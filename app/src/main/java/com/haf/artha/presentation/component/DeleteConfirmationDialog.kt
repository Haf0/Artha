package com.haf.artha.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Konfirmasi Hapus") },
            text = { Text(text = "Apakah anda yakin ingin menghapus item ini?") },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = "Hapus", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Batal")
                }
            }
        )
    }
}


@Preview
@Composable
private fun preview() {
    DeleteConfirmationDialog(showDialog = true, onConfirm = {}, onDismiss = {})
}