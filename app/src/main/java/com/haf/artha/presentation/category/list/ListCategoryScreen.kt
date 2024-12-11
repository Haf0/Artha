@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haf.artha.data.local.entity.CategoryEntity
import com.haf.artha.presentation.category.list.ListCategoryViewModel
import com.haf.artha.presentation.category.list.component.CategoryItem
import com.haf.artha.presentation.category.list.component.ColorOptionsRow
import com.haf.artha.presentation.category.list.component.positionAwareImePadding
import com.haf.artha.presentation.common.UiState
import com.haf.artha.presentation.component.LoadingIndicator
import com.haf.artha.ui.theme.listColorOption
import kotlinx.coroutines.launch

@Composable
fun ListCategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: ListCategoryViewModel = hiltViewModel()
) {
    val categoryList by viewModel.categoryList.collectAsState()
    var openBottomSheet by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    when(categoryList){
        is UiState.Loading -> {
            LoadingIndicator()
        }
        is UiState.Error -> {
            Text("Terjadi Kesalahan")
        }
        is UiState.Success -> {
           Box(
               Modifier
                   .fillMaxSize()
                   .clickable(
                       enabled = true,
                       onClickLabel = null,
                       interactionSource = remember { MutableInteractionSource() },
                       indication = null
                   ) {
                       keyboardController?.hide()
                   }
           ){
                CategoryItem(
                    modifier.padding(bottom = 56.dp),
                    items = (categoryList as UiState.Success).data.sortedBy { it.name }
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    onClick = {
                        openBottomSheet = true
                    }
                ) {
                    Text("Tambah Kategori")
                }
            }
            AddNewCategoryModal(
                openBottomSheet = openBottomSheet,
                onDismissRequest = { openBottomSheet = false }
            )

        }
    }
}



@Composable
fun AddNewCategoryModal(
    openBottomSheet : Boolean,
    onDismissRequest: () -> Unit
) {
    val density = LocalDensity.current
    val bottomSheetState = remember {
        SheetState(
            skipPartiallyExpanded = false,
            density = density
        )
    }
    
    if (openBottomSheet) {
        BottomSheetContent(
            bottomSheetState = bottomSheetState,
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
fun BottomSheetContent(
    bottomSheetState: SheetState,
    onDismissRequest: () -> Unit,
    viewModel: ListCategoryViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var showColorOptions by remember { mutableStateOf(false) }
    val colorOption = listColorOption
    var colorSelected by remember { mutableStateOf(colorOption[0]) }
    var newCategory by remember { mutableStateOf(emptyList<CategoryEntity>()) }
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 48.dp)
                .positionAwareImePadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp, end = 8.dp)
                        .clip(CircleShape)
                        .size(40.dp)
                        .background(colorSelected)
                        .border(1.dp, Color.Black, CircleShape)
                        .clickable {
                            showColorOptions = !showColorOptions
                        }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Kategori Baru") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    }),
                )
            }
            if(showColorOptions){
                ColorOptionsRow(
                    colors =colorOption,
                    onColorSelected = {
                        showColorOptions = false
                        colorSelected = it
                    }
                )
            }
            val context = LocalContext.current
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    if (name.isNotEmpty()||name.isNotBlank()){
                        newCategory = listOf(CategoryEntity(name = name, color = colorSelected.toArgb()))
                        viewModel.insertCategory(newCategory)
                        scope
                            .launch { bottomSheetState.hide() }
                            .invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    onDismissRequest()
                                }
                            }
                    }else{
                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Tambah Kategori Baru")
            }
        }

    }
}