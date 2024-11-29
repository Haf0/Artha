@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haf.artha.presentation.category.list.ListCategoryViewModel
import com.haf.artha.presentation.category.list.component.CategoryItem
import com.haf.artha.presentation.common.UiState
import com.haf.artha.presentation.component.LoadingIndicator
import kotlinx.coroutines.launch

@Composable
fun ListCategoryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ListCategoryViewModel = hiltViewModel()
) {
    val categoryList by viewModel.categoryList.collectAsState()


    when(categoryList){
        is UiState.Loading -> {
            LoadingIndicator()
        }
        is UiState.Error -> {
            // Log.d("homeScreen", "HomeScreen: loading")
        }
        is UiState.Success -> {
            CategoryItem(
                items = (categoryList as UiState.Success).data
            )
            AddNewCategoryModal()

        }
    }
}



@Composable
fun AddNewCategoryModal() {
    var openBottomSheet by remember { mutableStateOf(true) }
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
            onDismissRequest = { openBottomSheet = false }
        )
    }
}

@Composable
fun BottomSheetContent(
    bottomSheetState: SheetState,
    onDismissRequest: () -> Unit,
    viewModel: ListCategoryViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {
                    scope
                        .launch { bottomSheetState.hide() }
                        .invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                onDismissRequest()
                            }
                        }
                }
            ) {
                Text("Hide Bottom Sheet")
            }
        }
        var text by remember { mutableStateOf("") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.padding(horizontal = 16.dp),
            label = { Text("Text field") }
        )
    }
}


