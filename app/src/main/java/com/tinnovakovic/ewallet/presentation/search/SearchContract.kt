package com.tinnovakovic.ewallet.presentation.search

import com.tinnovakovic.ewallet.shared.mvi.BaseUiEvent
import com.tinnovakovic.ewallet.shared.mvi.BaseUiState
import com.tinnovakovic.ewallet.shared.mvi.BaseViewModel
import javax.annotation.concurrent.Immutable

interface SearchContract {

    abstract class ViewModel : BaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val searchText: String,
        val searchResultsModel: SearchResultsModel,
        val isLoading: Boolean
    ) : BaseUiState {}

    sealed class UiEvents : BaseUiEvent {
        data object Initialise: UiEvents()
        data object UpButtonClicked : UiEvents()
        data class SearchTextChanged(val searchText: String) : UiEvents()

    }
}