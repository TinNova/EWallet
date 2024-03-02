package com.tinnovakovic.ewallet.domain

import java.util.Locale
import javax.inject.Inject

class GetTokensWithBalancesUseCase @Inject constructor(
    private val getTopTokensUseCase: GetTopTokensUseCase,
    private val getLatestTokensUseCase: GetLatestTokenBalancesUseCase,
    private val searchInMemoryCache: SearchInMemoryCache
) {

    suspend fun execute(searchText: String): List<TokenBalance> {
        val searchCache: SearchCache = searchInMemoryCache.cache.value

        // savedSearch letter is blank, do a network call
        return if (searchCache.savedSearchStartedWith.isBlank()) {
            searchCache.savedSearchStartedWith = searchText.first().toString()
            val tokens = getTopTokensUseCase.execute()
            val tokensFilteredByFirstLetter = tokens.filter {
                it.symbol.startsWith(searchText.uppercase(Locale.getDefault()).first())
            }

            val latestTokenBalances = getLatestTokensUseCase.execute(tokensFilteredByFirstLetter)
            searchCache.savedTokenBalances.addAll(latestTokenBalances)
            val filteredTokenBalances =
                latestTokenBalances.filter { it.symbol.startsWith(searchText.uppercase(Locale.getDefault())) }
            filteredTokenBalances

            // searchText starts with the same letter as savedSearch, just filter
        } else if (searchText.isNotBlank() && searchCache.savedSearchStartedWith == searchText.first()
                .toString()
        ) {
            searchCache.savedSearchStartedWith = searchText.first().toString()
            val filteredTokenBalances =
                searchCache.savedTokenBalances.filter { it.symbol.startsWith(searchText.uppercase(Locale.getDefault())) }
            filteredTokenBalances

            // searchText is blank, return an empty list and update saveSearch
        } else {
            searchCache.savedSearchStartedWith = ""
            searchCache.savedTokenBalances.clear()
            emptyList()
        }
    }

    data class SearchCache(
        var savedSearchStartedWith: String = "",
        var savedTokenBalances: MutableList<TokenBalance> = mutableListOf()
    )

}
