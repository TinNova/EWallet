package com.tinnovakovic.ewallet.presentation.home

import androidx.annotation.MainThread
import com.tinnovakovic.ewallet.R
import com.tinnovakovic.ewallet.shared.ContextProvider
import com.tinnovakovic.ewallet.shared.NavDirection
import com.tinnovakovic.ewallet.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navManager: NavManager,
    private val contextProvider: ContextProvider
) : HomeContract.ViewModel() {

    override val _uiState: MutableStateFlow<HomeContract.UiState> =
        MutableStateFlow(initialUiState())

    private var initializeCalled = false

    @MainThread
    private fun initialise() {
        if (initializeCalled) return
        initializeCalled = true

        displayWalletAddress()

    }

    override fun onUiEvent(event: HomeContract.UiEvents) {
        when (event) {
            HomeContract.UiEvents.Initialise -> initialise()

            is HomeContract.UiEvents.ButtonClicked -> {
                navManager.navigate(direction = NavDirection.searchScreen())
            }

        }
    }

    private fun displayWalletAddress() {
        val walletAddress = contextProvider.getContext().getString(R.string.wallet_address)

        val firstSixChars = walletAddress.take(6)
        val lastSixChars = walletAddress.takeLast(6)
        val formattedWalletAddress = "$firstSixChars...$lastSixChars"

        updateUiState {
            it.copy(walletAddress = formattedWalletAddress)
        }
    }

    companion object {
        fun initialUiState() = HomeContract.UiState(
            walletAddress = ""
        )
    }
}
