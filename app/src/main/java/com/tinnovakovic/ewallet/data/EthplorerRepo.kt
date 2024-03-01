package com.tinnovakovic.ewallet.data

import com.tinnovakovic.ewallet.shared.RetryIo
import retrofit2.HttpException
import javax.inject.Inject

class EthplorerRepo @Inject constructor(
    private val ethplorerApi: EthplorerApi,
    private val retryIo: RetryIo
) {

    suspend fun getTopTokens(): Result<TopTokensData> {
        return ethplorerApi
            .getTopTokens()
            .recover { handleRetry(it) }
    }

    private suspend fun handleRetry(
        throwable: Throwable
    ): TopTokensData {

        if (throwable is HttpException && throwable.code() == 429) {
            return retryIo.retryIO {
                ethplorerApi.getTopTokens()
            }.getOrElse {
                throw it
            }
        } else {
            throw throwable
        }
    }

}
