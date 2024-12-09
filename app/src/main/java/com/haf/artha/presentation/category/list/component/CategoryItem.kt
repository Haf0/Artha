package com.haf.artha.presentation.category.list.component


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haf.artha.data.local.entity.CategoryEntity
import com.haf.artha.presentation.category.list.ListCategoryViewModel
import com.haf.artha.presentation.component.DeleteConfirmationDialog
import com.haf.artha.ui.theme.listColorOption

@Composable
fun CategoryItem(
    modifier: Modifier,
    items: List<CategoryEntity>,
    viewModel: ListCategoryViewModel = hiltViewModel()
) {
    val editState = remember { mutableStateMapOf<Int, Boolean>() }
    val itemTexts = remember { mutableStateListOf(*items.filter { it.name != "Transfer" }.toTypedArray()) }
    val showColorOptions = remember { mutableStateMapOf<Int, Boolean>() }
    val colorsOption = listColorOption
    val keyboardController = LocalSoftwareKeyboardController.current
    var name by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(1) }
    var showDialog by remember { mutableStateOf(false) }
    var id by remember { mutableStateOf(99999) }
    var indexes by remember { mutableStateOf(99999) }
    DeleteConfirmationDialog(
        showDialog = showDialog,
        onConfirm = {
            viewModel.deleteCategoryById(id)
            Log.d("categoryScree", "CategoryItem: delete ${id}")
            itemTexts.removeAt(indexes)
            showDialog = false
        },
        onDismiss = {
            showDialog = false
        }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                enabled = true,
                onClickLabel = null,
                interactionSource = remember { MutableInteractionSource() },
                indication = null // This removes the ripple effect
            ) {
                editState.forEach {
                    editState[it.key] = false
                }
            }
    ) {
        itemsIndexed(itemTexts) { index, item ->
            val isEditing = editState[index] ?: false
            val isColorOptionsVisible = showColorOptions[index] ?: false
            val oldCategoryEntity = CategoryEntity(id = item.id, name = item.name, color = item.color)
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
                                .background(Color(item.color))
                                .border(1.dp, Color.Black, CircleShape)
                                .clickable(enabled = isEditing) {
                                    showColorOptions[index] = !isColorOptionsVisible
                                }
                        )
                        if (isEditing) {
                            OutlinedTextField(
                                value = item.name,
                                onValueChange = {
                                    itemTexts[index] = item.copy(name = it)
                                    name = it
                                },
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
                                text = item.name,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row {
                            IconButton(onClick = {
                                editState[index] = !isEditing
                                Log.d("categoryscreen", "CategoryItem:  ${oldCategoryEntity.name == name}, ${oldCategoryEntity.color == color}")
                                if (isEditing ) {
                                    if (oldCategoryEntity.name != name || oldCategoryEntity.color != color) {
                                        viewModel.updateCategory(itemTexts[index])
                                    }

                                }
                            }) {
                                Icon(
                                    imageVector = if (isEditing) Icons.Filled.Done else Icons.Outlined.Edit,
                                    contentDescription = if (isEditing) "Save" else "Edit"
                                )
                            }

                            IconButton(onClick = {
                                id = item.id
                                indexes = index
                                showDialog = true
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    }
                    if(isColorOptionsVisible && isEditing){
                        ColorOptionsRow(
                            colors =colorsOption,
                            onColorSelected = {
                                itemTexts[index] = item.copy(color = it.toArgb())
                                showColorOptions[index] = false
                                color = it.toArgb()
                            }
                        )
                    }
                }
            }


        }
    }
}

