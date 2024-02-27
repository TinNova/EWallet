package com.tinnovakovic.ewallet.presentation.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchScreen() {
    val viewModel = hiltViewModel<SearchViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreenContent(
        uiState = uiState,
        uiAction = viewModel::onUiEvent,
    )
}

@Composable
fun SearchScreenContent(
    uiState: SearchContract.UiState,
    uiAction: (SearchContract.UiEvents) -> Unit,
) {
    
    Text(text = "SearchScreen")

}