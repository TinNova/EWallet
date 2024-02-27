package com.tinnovakovic.ewallet.presentation.search

import com.tinnovakovic.ewallet.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val navManager: NavManager
) : SearchContract.ViewModel() {

    override val _uiState: MutableStateFlow<SearchContract.UiState> =
        MutableStateFlow(initialUiState())

    override fun onUiEvent(event: SearchContract.UiEvents) {
        when (event) {
            is SearchContract.UiEvents.ButtonClicked -> {
//                navManager.navigate(directions = NavDirection.searchScreen())
            }
        }
    }

    companion object {
        fun initialUiState() = SearchContract.UiState(
            number = 0
        )
    }
}