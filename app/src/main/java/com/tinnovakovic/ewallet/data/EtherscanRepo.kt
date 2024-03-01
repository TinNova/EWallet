package com.tinnovakovic.ewallet.data

import com.tinnovakovic.ewallet.shared.RetryIo
import retrofit2.HttpException
import javax.inject.Inject

class EtherscanRepo @Inject constructor(
    private val etherscanApi: EtherscanApi,
    private val retryIo: RetryIo
) {

    suspend fun getLatestTokenBalance(
        walletAddress: String,
        tokenAddress: String
    ): Result<TokenBalanceData> {
        return etherscanApi
            .getLatestTokenBalance(
                walletAddress = walletAddress,
                tokenAddress = tokenAddress
            )
            .recover { throwable ->
                handleRetry(throwable, walletAddress, tokenAddress)
            }
    }

    private suspend fun handleRetry(
        throwable: Throwable,
        walletAddress: String,
        tokenAddress: String
    ): TokenBalanceData {

        if (throwable is HttpException && throwable.code() == 429) {
            return retryIo.retryIO {
                etherscanApi.getLatestTokenBalance(
                    walletAddress = walletAddress,
                    tokenAddress = tokenAddress
                )
            }.getOrElse {
                throw it
            }
        } else {
            throw throwable
        }
    }
}
