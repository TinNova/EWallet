package com.tinnovakovic.ewallet.presentation.home

import com.tinnovakovic.ewallet.shared.mvi.BaseUiEvent
import com.tinnovakovic.ewallet.shared.mvi.BaseUiState
import com.tinnovakovic.ewallet.shared.mvi.BaseViewModel
import javax.annotation.concurrent.Immutable

interface HomeContract {

    abstract class ViewModel : BaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val number: Int
    ) : BaseUiState {}

    sealed class UiEvents : BaseUiEvent {
        data object ButtonClicked : UiEvents()
    }
}