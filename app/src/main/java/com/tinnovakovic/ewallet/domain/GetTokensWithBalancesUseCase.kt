package com.tinnovakovic.ewallet.domain

import com.tinnovakovic.ewallet.R
import com.tinnovakovic.ewallet.data.AddressInfoData
import com.tinnovakovic.ewallet.data.EthplorerRepo
import com.tinnovakovic.ewallet.shared.ContextProvider
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetTokensWithBalancesUseCase @Inject constructor(
    private val ethplorerRepo: EthplorerRepo,
    private val getTopTokensUseCase: GetTopTokensUseCase,
    private val contextProvider: ContextProvider
) {

    // DONE - first, onSearch: check InMemoryCache if contains list of tokens, if false, download list of tokens and save to InMemoryCache

    // DONE - second, onSearch: filter the token list from InMemoryCache using contains()

    // DONE - thirdly, and do a network call on that

    // Fourthly, consider returning the results as a flow instead of all at once, this could help with the rate limit, as we can display what we can, then if we hit the limit we can do an exponential back off while retrieving the rest
    suspend fun execute(tokens: List<Token>): List<TokenBalance> {
        return getTokenBalances(tokens)
    }

    private suspend fun getTokenBalances(filteredTokens: List<Token>): List<TokenBalance> {


        return filteredTokens.mapNotNull {
            val addressInfoData = ethplorerRepo.getAddressInfo(
                walletAddress = contextProvider.getContext().getString(R.string.wallet_address),
                tokenAddress = it.address
            )

            val tokenBalances = sanitiseAndMapToTokenBalance(addressInfoData)
            tokenBalances
        }
    }

    private fun sanitiseAndMapToTokenBalance(addressInfoData: AddressInfoData): TokenBalance? {

        val ethBalance: String = addressInfoData.eTH?.balance ?: return null
        val token = if (!addressInfoData.tokens.isNullOrEmpty()) addressInfoData.tokens.first() else return null
        val symbol = token.symbol ?: return null

        return TokenBalance(
            symbol = symbol,
            tokenAddress = token.address,
            balance = ethBalance
        )
    }

    companion object {
        const val DELAY_IN_MILLIS = 400L
    }
}
