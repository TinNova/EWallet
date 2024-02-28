package com.tinnovakovic.ewallet.domain

import com.tinnovakovic.ewallet.R
import com.tinnovakovic.ewallet.data.EtherscanRepo
import com.tinnovakovic.ewallet.shared.ContextProvider
import kotlinx.coroutines.delay
import java.math.BigInteger
import javax.inject.Inject

class GetTokensWithBalancesUseCase @Inject constructor(
    private val etherscanRepo: EtherscanRepo,
    private val contextProvider: ContextProvider,
    private val fromSmallestDecimalRepresentationUseCase: FromSmallestDecimalRepresentationUseCase
) {

    suspend fun execute(tokens: List<Token>): List<TokenBalance> {

        return tokens.map {
            val tokenBalanceData = etherscanRepo.getLatestTokenBalance(
                walletAddress = contextProvider.getContext().getString(R.string.wallet_address),
                tokenAddress = it.address
            )

            val tokenBalance = TokenBalance(
                symbol = it.symbol, //might have to remove nulls
                result = fromSmallestDecimalRepresentationUseCase
                    .execute(
                        smallestBalance = tokenBalanceData.result.toBigInteger(),
                        decimalPlaces = it.decimals.toInt()
                    ),
                isResultZero = tokenBalanceData.result.toBigInteger() == BigInteger.ZERO
            )

            if (tokens.size >= 5) delay(DELAY_IN_MILLIS)
            tokenBalance
        }
    }

    companion object {
        const val DELAY_IN_MILLIS = 200L // allows for a maximum of 5 calls per second
    }
}
