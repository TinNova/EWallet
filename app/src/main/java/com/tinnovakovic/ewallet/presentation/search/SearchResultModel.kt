package com.tinnovakovic.ewallet.presentation.search

import com.tinnovakovic.ewallet.domain.TokenBalance

sealed class SearchResultsModel {
    data object Prompt : SearchResultsModel()
    data object NoResults : SearchResultsModel()
    data class Error(val errorMessage: String) : SearchResultsModel()
    data class Success(
        val tokenBalances: List<TokenBalance>,
    ) : SearchResultsModel()
}