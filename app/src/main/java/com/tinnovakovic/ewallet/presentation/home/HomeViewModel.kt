package com.tinnovakovic.ewallet.presentation.home

import com.tinnovakovic.ewallet.shared.NavDirection
import com.tinnovakovic.ewallet.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navManager: NavManager
) : HomeContract.ViewModel() {

    override val _uiState: MutableStateFlow<HomeContract.UiState> =
        MutableStateFlow(initialUiState())

    override fun onUiEvent(event: HomeContract.UiEvents) {
        when (event) {
            is HomeContract.UiEvents.ButtonClicked -> {
                navManager.navigate(direction = NavDirection.searchScreen())
            }
        }
    }

    companion object {
        fun initialUiState() = HomeContract.UiState(
            number = 0
        )
    }
}
