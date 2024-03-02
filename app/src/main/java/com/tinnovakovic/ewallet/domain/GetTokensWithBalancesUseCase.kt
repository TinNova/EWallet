package com.tinnovakovic.ewallet.domain

import com.tinnovakovic.ewallet.R
import com.tinnovakovic.ewallet.data.EtherscanRepo
import com.tinnovakovic.ewallet.data.TokenBalanceData
import com.tinnovakovic.ewallet.shared.ContextProvider
import com.tinnovakovic.ewallet.shared.RateLimitHandler
import java.math.BigInteger
import java.util.Locale
import javax.inject.Inject

class GetTokensWithBalancesUseCase @Inject constructor(
    private val getTopTokensUseCase: GetTopTokensUseCase,
    private val getLatestTokensUseCase: GetLatestTokensUseCase
) {

    //TODO: Refactor this, currently it's not testable and breaks single source of truth
    private var savedSearchStartedWith: String = ""
    private var savedTokenBalances: MutableList<TokenBalance> = mutableListOf()

    suspend fun execute(searchText: String): List<TokenBalance> {
        // savedSearch letter is blank, do a network call
        return if (savedSearchStartedWith.isBlank()) {
            savedSearchStartedWith = searchText.first().toString()
            val tokens = getTopTokensUseCase.execute()
            val tokensFilteredByFirstLetter = tokens.filter {
                it.symbol.startsWith(searchText.uppercase(Locale.getDefault()).first())
            }

            val latestTokenBalances = getLatestTokensUseCase.execute(tokensFilteredByFirstLetter)
            savedTokenBalances.addAll(latestTokenBalances)
            val filteredTokenBalances =
                latestTokenBalances.filter { it.symbol.startsWith(searchText.uppercase(Locale.getDefault())) }
            filteredTokenBalances

            // searchText starts with the same letter as savedSearch, just filter
        } else if (searchText.isNotBlank() && savedSearchStartedWith == searchText.first()
                .toString()
        ) {
            savedSearchStartedWith = searchText.first().toString()
            val filteredTokenBalances =
                savedTokenBalances.filter { it.symbol.startsWith(searchText.uppercase(Locale.getDefault())) }
            filteredTokenBalances

            // searchText is blank, return an empty list and update saveSearch
        } else {
            savedSearchStartedWith = ""
            savedTokenBalances.clear()
            emptyList()
        }
    }
}
