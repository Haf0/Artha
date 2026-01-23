package com.haf.artha.presentation.category.list.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haf.artha.data.local.entity.CategoryEntity
import com.haf.artha.presentation.category.list.ListCategoryViewModel
import com.haf.artha.presentation.common.UiState
import com.haf.artha.presentation.component.DeleteConfirmationDialog
import com.haf.artha.ui.theme.listColorOption

@Composable
fun CategoryItem(
    modifier: Modifier,
    viewModel: ListCategoryViewModel = hiltViewModel()
) {
    val uiSuccess by viewModel.categoryList.collectAsState()
    val allItems = ((uiSuccess as? UiState.Success)?.data ?: emptyList()).sortedBy { it.name.lowercase() }

    val displayItems = remember(allItems) {
        allItems.filter { it.name != "Transfer" }
    }

    val colorsOption = listColorOption
    val keyboardController = LocalSoftwareKeyboardController.current

    var editingId by remember { mutableIntStateOf(-1) }

    var tempName by remember { mutableStateOf("") }
    var tempColor by remember { mutableIntStateOf(0) }
    var isColorPickerOpen by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDeleteId by remember { mutableIntStateOf(-1) }

    DeleteConfirmationDialog(
        showDialog = showDeleteDialog,
        onConfirm = {
            viewModel.deleteCategoryById(itemToDeleteId)
            showDeleteDialog = false
            if (editingId == itemToDeleteId) {
                editingId = -1
            }
        },
        onDismiss = {
            showDeleteDialog = false
        }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(
            items = displayItems,
            key = { it.id }
        ) { item ->
            val isEditing = editingId == item.id

            val displayName = if (isEditing) tempName else item.name
            val displayColor = if (isEditing) tempColor else item.color
            val context = LocalContext.current

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clip(CircleShape)
                                .size(30.dp)
                                .background(Color(displayColor))
                                .border(1.dp, Color.Black, CircleShape)
                                .clickable(enabled = isEditing) {
                                    isColorPickerOpen = !isColorPickerOpen
                                }
                        )
                        if (isEditing) {
                            OutlinedTextField(
                                value = tempName,
                                onValueChange = { tempName = it },
                                label = { Text("Kategori") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = {
                                    keyboardController?.hide()
                                }),
                                maxLines = 1
                            )
                        } else {
                            Text(
                                text = displayName,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row {
                            IconButton(onClick = {
                                if (isEditing) {
                                    if (item.name.isEmpty()|| item.name.isBlank()){
                                        Toast.makeText(context, "tidak boleh kosong", Toast.LENGTH_SHORT).show()
                                    }else if (item.name != tempName || item.color != tempColor) {
                                        viewModel.updateCategory(item.copy(name = tempName, color = tempColor))
                                    }
                                    editingId = -1
                                    isColorPickerOpen = false
                                } else {
                                    editingId = item.id
                                    tempName = item.name
                                    tempColor = item.color
                                    isColorPickerOpen = false
                                }
                            }) {
                                Icon(
                                    imageVector = if (isEditing) Icons.Filled.Done else Icons.Outlined.Edit,
                                    contentDescription = if (isEditing) "Save" else "Edit"
                                )
                            }

                            IconButton(onClick = {
                                if (isEditing) {
                                    editingId = -1
                                    isColorPickerOpen = false
                                } else {
                                    itemToDeleteId = item.id
                                    showDeleteDialog = true
                                }
                            }) {
                                Icon(
                                    imageVector = if (!isEditing) Icons.Filled.Delete else Icons.Filled.Close,
                                    contentDescription = if (!isEditing) "Delete" else "Cancel"
                                )
                            }
                        }
                    }

                    if (isEditing && isColorPickerOpen) {
                        ColorOptionsRow(
                            colors = colorsOption,
                            onColorSelected = {
                                tempColor = it.toArgb()
                                isColorPickerOpen = false
                            }
                        )
                    }
                }
            }
        }
    }
}
