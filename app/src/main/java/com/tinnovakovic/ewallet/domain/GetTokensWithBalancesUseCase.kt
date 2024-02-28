package com.tinnovakovic.ewallet.domain

import com.tinnovakovic.ewallet.R
import com.tinnovakovic.ewallet.data.AddressInfoData
import com.tinnovakovic.ewallet.data.EthplorerRepo
import com.tinnovakovic.ewallet.shared.ContextProvider
import javax.inject.Inject

class GetTokensWithBalancesUseCase @Inject constructor(
    private val ethplorerRepo: EthplorerRepo,
    private val getTopTokensUseCase: GetTopTokensUseCase,
    private val contextProvider: ContextProvider
) {

    // DONE - first, onSearch: check InMemoryCache if contains list of tokens, if false, download list of tokens and save to InMemoryCache

    // DONE - second, onSearch: filter the token list from InMemoryCache using contains()

    // thirdly, and do a network call on that

    // thirdly, consider returning the results as a flow instead of all at once, this could help with the rate limit, as we can display what we can, then if we hit the limit we can do an exponential back off while retrieving the rest
    suspend fun execute(filteredTokens: List<Token>): List<TokenBalance> {
        return getTokenBalances(filteredTokens)
    }

    private suspend fun getTokenBalances(filteredTokens: List<Token>): List<TokenBalance> {
        return filteredTokens.mapNotNull {
            val addressInfoData = ethplorerRepo.getAddressInfo(
                walletAddress = contextProvider.getContext().getString(R.string.wallet_address),
                tokenAddress = it.address
            )

            sanitiseAndMapToTokenBalance(addressInfoData)

        }
        // Exception 429
    }

    private fun sanitiseAndMapToTokenBalance(addressInfoData: AddressInfoData): TokenBalance? {
        return if (
            addressInfoData.eTH != null &&
            addressInfoData.tokens.first().symbol != null &&
            addressInfoData.tokens.isNotEmpty()
        ) {
            TokenBalance(
                symbol = addressInfoData.tokens.first().symbol,
                tokenAddress = addressInfoData.address,
                balance = addressInfoData.eTH.balance ?: "n/a"
            )
        } else {
            null
        }
    }
}