package com.tinnovakovic.ewallet.presentation.search

import com.tinnovakovic.ewallet.shared.mvi.BaseUiEvent
import com.tinnovakovic.ewallet.shared.mvi.BaseUiState
import com.tinnovakovic.ewallet.shared.mvi.BaseViewModel
import javax.annotation.concurrent.Immutable

interface SearchContract {

    abstract class ViewModel : BaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val number: Int
    ) : BaseUiState {}

    sealed class UiEvents : BaseUiEvent {
        data object ButtonClicked : UiEvents()
    }
}