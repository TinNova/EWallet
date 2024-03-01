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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.input.ImeAction
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
                                Text(text = "Search For A Token") //Consider that search bar animation here...
//                            InitialSearchScreen(R.string.search_initial_empty_message)
                            }
                        }

                        uiState.searchResultsModel is SearchResultsModel.NoResults -> {
                            item {
                                Text(text = "No Tokens Found")
                            }
                        }

                        uiState.searchResultsModel is SearchResultsModel.Error -> {
                            item {
                                Text(text = uiState.searchResultsModel.errorMessage)
                            }
                        }

                        uiState.searchResultsModel is SearchResultsModel.Success -> {
                            val tokenBalances = uiState.searchResultsModel.tokenBalances

                            items(count = tokenBalances.size) { index ->
                                val tokenBalance = tokenBalances[index]
                                TokenBalanceItem(tokenBalance)
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
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.size.extraLarge,
                vertical = MaterialTheme.size.medium
            )
    ) {
        Text(
            text = "${tokenBalance.symbol} Balance:",
            fontWeight = W600
        )
        Text(
            text = "${tokenBalance.result} ${tokenBalance.symbol}",
            color = if (tokenBalance.isResultZero) Red else Green
        )
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
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )

    // To initially have focus on the search field and bring up the keyboard
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}