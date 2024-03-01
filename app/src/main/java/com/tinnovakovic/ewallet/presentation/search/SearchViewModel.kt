package com.tinnovakovic.ewallet.presentation.search

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.viewModelScope
import com.tinnovakovic.ewallet.domain.FilterListOfTokensUseCase
import com.tinnovakovic.ewallet.domain.GetTokensWithBalancesUseCase
import com.tinnovakovic.ewallet.shared.ExceptionHandler
import com.tinnovakovic.ewallet.shared.NavDirection
import com.tinnovakovic.ewallet.shared.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val navManager: NavManager,
    private val getTokensWithBalancesUseCase: GetTokensWithBalancesUseCase,
    private val filterListOfTokensUseCase: FilterListOfTokensUseCase,
    private val exceptionHandler: ExceptionHandler,
) : SearchContract.ViewModel() {

    override val _uiState: MutableStateFlow<SearchContract.UiState> =
        MutableStateFlow(defaultUiState)

    override fun onUiEvent(event: SearchContract.UiEvents) {
        when (event) {
            is SearchContract.UiEvents.UpButtonClicked -> {
                navManager.navigate(direction = NavDirection.homeScreen)
            }

            is SearchContract.UiEvents.SearchTextChanged -> onSearchTextChanged(event.searchText)
        }
    }

    private fun onSearchTextChanged(searchText: String) {
        Log.d("TINTINTEST", "search text: $searchText")
        viewModelScope.launch(coExceptionHandler) {
            updateUiState {
                it.copy(
                    isLoading = true, searchText = searchText
                )
            }

            val tokenBalances = getTokensWithBalancesUseCase.execute(searchText)
            val searchResultModel = if (tokenBalances.isNotEmpty()) {
                 SearchResultsModel.Success(tokenBalances)
            } else {
                SearchResultsModel.NoResults
            }
            updateUiState {
                it.copy(
                    searchResultsModel = searchResultModel,
                    isLoading = false,
                    searchText = searchText
                )
            }
        }
    }

    private val coExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = exceptionHandler.getErrorMessage(throwable)

        updateUiState {
            it.copy(
                isLoading = false,
                searchResultsModel = SearchResultsModel.Error(errorMessage)
            )
        }
    }

    companion object {
        private val defaultUiState = SearchContract.UiState(
            searchText = "",
            searchResultsModel = SearchResultsModel.Prompt,
            isLoading = false
        )

        private const val SEARCH_INPUT_DEBOUNCE_MILLIS = 300L // half a second
        private const val MINIMUM_INPUT_LENGTH = 1
        private const val EMPTY_STRING = ""
    }
}