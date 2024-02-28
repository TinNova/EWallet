package com.tinnovakovic.ewallet.data

import javax.inject.Inject

class EthplorerRepo @Inject constructor(
    private val ethplorerApi: EthplorerApi
) {

    suspend fun getTopTokens(): TopTokensData {
        return ethplorerApi.getTopTokens()
    }

}
