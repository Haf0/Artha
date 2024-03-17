package com.haf.artha.presentation.onboarding.setCurrency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haf.artha.R


@Composable
fun SetCurrencyScreen(
    viewModel: SetCurrencyViewModel = hiltViewModel()
) {

    viewModel.groupedCurrency.let {
        SetCurrencyContent(
            listCurrency = it.value
        )
    }

}


@Composable
fun SetCurrencyContent(
    listCurrency: List<AvailableCurrency>,
    modifier: Modifier = Modifier
) {

    var selectedCurrency by remember { mutableStateOf(getDefaultCurrency()) }
    val selectedIndex = remember {
        mutableStateOf(-1)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = stringResource(R.string.select_currency), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = modifier.height(16.dp))
        DefaultCurrency(selectedCurrency)
        Spacer(modifier = modifier.height(8.dp))

        val textState = remember {
            mutableStateOf(TextFieldValue())
        }
        SearchView(
            state = textState,
            placeHolder = stringResource(R.string.search_currency),
            modifier = modifier
        )
        val searchedText = textState.value.text
        val filteredList = if (searchedText.isEmpty()) {
            listCurrency
        } else {
            listCurrency.filter {
                it.name.contains(searchedText, ignoreCase = true) || it.code.contains(searchedText, ignoreCase = true)
            }
        }
        LazyColumn {
            filteredList.forEach { currency ->
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .padding(top = 8.dp, bottom = 8.dp)
                            .selectable(
                                selected = currency == selectedCurrency,
                                onClick = {
                                    selectedCurrency = currency
                                    selectedIndex.value = listCurrency.indexOf(currency)
                                }
                            )
                            .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                            .background(if (currency == selectedCurrency) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)

                    ) {

                        Column {
                            Text(
                                text = currency.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                            Text(text = currency.code,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )

                        }

                    }
                }
            }
        }
    }

}


@Composable
fun DefaultCurrency(
    currency: AvailableCurrency
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
        , elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = currency.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top=8.dp)
            )
            Text(text = currency.code,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    placeHolder: String,
    modifier: Modifier
) {
    TextField(
        value = state.value,
        onValueChange = {value->
            state.value = value
        },
        modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(1.dp, Color.DarkGray, RoundedCornerShape(15.dp)),
        placeholder = {
            Text(text = placeHolder)
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White
        ),
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(
            color = Color.Black, fontSize = 20.sp
        )
    )

}