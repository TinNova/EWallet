package com.tinnovakovic.ewallet.domain

import com.tinnovakovic.ewallet.R
import com.tinnovakovic.ewallet.data.EtherscanRepo
import com.tinnovakovic.ewallet.data.TokenBalance
import com.tinnovakovic.ewallet.shared.ContextProvider
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetTokensWithBalancesUseCase @Inject constructor(
    private val etherscanRepo: EtherscanRepo,
    private val contextProvider: ContextProvider
) {

    suspend fun execute(tokens: List<Token>): List<TokenBalance> {

        return tokens.map {
            val tokenBalance = etherscanRepo.getLatestTokenBalance(
                walletAddress = contextProvider.getContext().getString(R.string.wallet_address),
                tokenAddress = it.address
            )
            if (tokens.size >= 5) delay(DELAY_IN_MILLIS)
            tokenBalance
        }
    }

    companion object {
        const val DELAY_IN_MILLIS = 200L // allows for a maximum of 5 calls per second
    }
}
