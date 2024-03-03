package com.tinnovakovic.ewallet.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tinnovakovic.ewallet.domain.TokenBalance
import com.tinnovakovic.ewallet.presentation.search.SearchContract.*
import com.tinnovakovic.ewallet.ui.theme.size

@Composable
fun SearchScreen() {
    val viewModel = hiltViewModel<SearchViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreenContent(
        uiState = uiState,
        uiAction = viewModel::onUiEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    uiState: UiState,
    uiAction: (UiEvents) -> Unit,
) {
    LaunchedEffect(true) {
        // This instead on using init{} in viewModel to prevent race condition
        uiAction(UiEvents.Initialise)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "EC20") },
                navigationIcon = {
                    IconButton(onClick = { uiAction(UiEvents.UpButtonClicked) }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { scaffoldPadding ->

        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            SearchTextField(uiState.searchText, uiAction)
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = MaterialTheme.size.medium),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {

                    when {
                        uiState.searchText.isBlank() || uiState.searchResultsModel is SearchResultsModel.Prompt -> {
                            item {
                                ErrorMessage("Search For A Token")
                            }
                        }

                        uiState.searchResultsModel is SearchResultsModel.NoResults -> {
                            item {
                                ErrorMessage("No Tokens Found")
                            }
                        }

                        uiState.searchResultsModel is SearchResultsModel.Error -> {
                            item {
                                ErrorMessage(uiState.searchResultsModel.errorMessage)
                            }
                        }

                        uiState.searchResultsModel is SearchResultsModel.Success -> {
                            val tokenBalances = uiState.searchResultsModel.tokenBalances

                            items(count = tokenBalances.size) { index ->
                                val tokenBalance = tokenBalances[index]
                                TokenBalanceItem(tokenBalance)
                                if (index < tokenBalances.size - 1) {
                                    HorizontalDivider(modifier = Modifier.padding(start = MaterialTheme.size.large))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TokenBalanceItem(tokenBalance: TokenBalance) {
    Column(
        modifier = Modifier.padding(
            top = MaterialTheme.size.medium,
            bottom = MaterialTheme.size.medium,
            start = MaterialTheme.size.large,
            end = MaterialTheme.size.medium
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.size.extraSmall)
        ) {
            Text(
                text = tokenBalance.symbol,
                fontWeight = W600,
            )

            Row {
                Text(
                    color = Gray,
                    text = "Currency | "
                )

                Text(
                    text = tokenBalance.symbol,
                    maxLines = 1,
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Balance",
                modifier = Modifier.padding(end = MaterialTheme.size.medium)
            )

            Text(
                text = tokenBalance.result,
                fontWeight = W400,
                color = if (tokenBalance.isResultZero) Red else Green,
                maxLines = 2,
                overflow = TextOverflow.Visible
            )
        }
    }
}

@Composable
private fun SearchTextField(searchText: String, uiAction: (UiEvents) -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchText,
        onValueChange = { uiAction(UiEvents.SearchTextChanged(it)) },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(
                top = MaterialTheme.size.medium,
                start = MaterialTheme.size.medium,
                end = MaterialTheme.size.medium,
            )
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        shape = RoundedCornerShape(MaterialTheme.size.medium),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )

    // To initially have focus on the search field and bring up the keyboard
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        modifier = Modifier.padding(MaterialTheme.size.large)
    )
}
