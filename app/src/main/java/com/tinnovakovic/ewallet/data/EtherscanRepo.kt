package com.tinnovakovic.ewallet.data

import android.util.Log
import com.tinnovakovic.ewallet.shared.EtherScanApiKeyException
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

        if (throwable is EtherScanApiKeyException.RateLimitException ||
            (throwable is HttpException && throwable.code() == 429)
        ) {

            return retryIo.retryIO {
                Log.d("TINTIN", "retry because of throwable: $throwable")
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
