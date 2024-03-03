package com.tinnovakovic.ewallet.presentation.search

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tinnovakovic.ewallet.R
import com.tinnovakovic.ewallet.data.NetworkInMemoryCache
import com.tinnovakovic.ewallet.domain.GetTokensWithBalancesUseCase
import com.tinnovakovic.ewallet.shared.ContextProvider
import com.tinnovakovic.ewallet.shared.ExceptionHandler
import com.tinnovakovic.ewallet.shared.navigation.NavDirection
import com.tinnovakovic.ewallet.shared.navigation.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val navManager: NavManager,
    private val getTokensWithBalancesUseCase: GetTokensWithBalancesUseCase,
    private val exceptionHandler: ExceptionHandler,
    private val networkInMemoryCache: NetworkInMemoryCache,
    private val savedStateHandle: SavedStateHandle,
    private val contextProvider: ContextProvider
) : SearchContract.ViewModel() {

    override val _uiState: MutableStateFlow<SearchContract.UiState> =
        MutableStateFlow(defaultUiState)

    private var initializeCalled = false
    private val searchQueryFlow = MutableStateFlow(uiState.value.searchText)
    private var searchJob: Job? = null

    @MainThread
    private fun initialise() {
        if (initializeCalled) return
        initializeCalled = true

        savedStateHandle.get<String>(SAVED_STATE_SEARCH_TEXT)?.let {
            onSearchTextChanged(it)
        }

        viewModelScope.launch {
            searchQueryFlow
                .collect {
                    searchJob?.cancel()
                    searchJob = search(it)
                }
        }
    }

    override fun onUiEvent(event: SearchContract.UiEvents) {
        when (event) {
            is SearchContract.UiEvents.Initialise -> initialise()

            is SearchContract.UiEvents.UpButtonClicked -> {
                navManager.navigate(direction = NavDirection.homeScreen)
            }

            is SearchContract.UiEvents.SearchTextChanged -> onSearchTextChanged(event.searchText)
        }
    }

    private fun onSearchTextChanged(searchText: String) {
        savedStateHandle[SAVED_STATE_SEARCH_TEXT] = searchText

        updateUiState {
            it.copy(
                isLoading = true, searchText = searchText
            )
        }

        searchQueryFlow.update { searchText }
    }

    suspend fun search(searchText: String): Job {
        return viewModelScope.launch(coExceptionHandler) {
            if (networkInMemoryCache.cache.value) {
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
            } else {
                updateUiState {
                    it.copy(
                        searchResultsModel = SearchResultsModel
                            .Error(
                                contextProvider.getContext().getString(
                                    R.string.io_error_message
                                )
                            ),
                        isLoading = false,
                        searchText = searchText
                    )
                }
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
            isLoading = false,
        )

        private const val SAVED_STATE_SEARCH_TEXT = "savedStateSearchText"
    }

}
