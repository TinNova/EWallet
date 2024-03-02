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
    private val etherscanRepo: EtherscanRepo,
    private val contextProvider: ContextProvider,
    private val fromSmallestDecimalRepresentationUseCase: FromSmallestDecimalRepresentationUseCase,
    private val rateLimitHandler: RateLimitHandler,
    private val getTopTokensUseCase: GetTopTokensUseCase
) {

    //TODO: Refactor this, currently it's not testable and breaks single source of truth
    var savedSearchStartedWith: String = ""
    var savedTokenBalances: MutableList<TokenBalance> = mutableListOf()

    suspend fun execute(searchText: String): List<TokenBalance> {
        // savedSearch letter is blank, do a network call
        return if (savedSearchStartedWith.isBlank()) {
            savedSearchStartedWith = searchText.first().toString()
            val tokens = getTopTokensUseCase.execute()
            val tokensFilteredByFirstLetter = tokens.filter {
                it.symbol.startsWith(searchText.uppercase(Locale.getDefault()).first())
            }

            val latestTokenBalances = getLatestTokenBalances(tokensFilteredByFirstLetter)
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

    private suspend fun getLatestTokenBalances(tokens: List<Token>): List<TokenBalance> {
        val tokenBalances: List<TokenBalance> = tokens.mapNotNull {
            val tokenBalanceData: TokenBalanceData? = etherscanRepo.getLatestTokenBalance(
                walletAddress = contextProvider.getContext().getString(R.string.wallet_address),
                tokenAddress = it.address
            ).getOrNull()

            if (tokenBalanceData != null) {
                val tokenBalance = TokenBalance(
                    symbol = it.symbol,
                    result = fromSmallestDecimalRepresentationUseCase
                        .execute(
                            smallestBalance = tokenBalanceData.result.toBigInteger(),
                            decimalPlaces = it.decimals.toInt()
                        ),
                    isResultZero = tokenBalanceData.result.toBigInteger() == BigInteger.ZERO
                )

                if (tokens.size >= 5) {
                    rateLimitHandler.rateLimitDelay()
                }

                savedTokenBalances.add(tokenBalance)
                tokenBalance
            } else {
                null// skip it if it's null
            }
        }

        return tokenBalances
    }
}
