package com.tinnovakovic.ewallet.domain

import com.tinnovakovic.ewallet.R
import com.tinnovakovic.ewallet.data.EtherscanRepo
import com.tinnovakovic.ewallet.data.TokenBalanceData
import com.tinnovakovic.ewallet.shared.ContextProvider
import com.tinnovakovic.ewallet.shared.RateLimitHandler
import java.math.BigInteger
import javax.inject.Inject

class GetLatestTokenBalancesUseCase @Inject constructor(
    private val etherscanRepo: EtherscanRepo,
    private val contextProvider: ContextProvider,
    private val fromSmallestDecimalRepresentationUseCase: FromSmallestDecimalRepresentationUseCase,
    private val rateLimitHandler: RateLimitHandler,
) {

    suspend fun execute(tokens: List<Token>): List<TokenBalance> {
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

                tokenBalance
            } else {
                null// skip it if it's null
            }
        }

        return tokenBalances
    }
}