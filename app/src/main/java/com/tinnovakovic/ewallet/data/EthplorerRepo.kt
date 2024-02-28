package com.tinnovakovic.ewallet.data

import javax.inject.Inject

class EthplorerRepo @Inject constructor(
    private val ethplorerApi: EthplorerApi
) {

    suspend fun getTopTokens(): TopTokensData {
        return ethplorerApi.getTopTokens()
    }

    suspend fun getAddressInfo(walletAddress: String, tokenAddress: String): AddressInfoData {
        return ethplorerApi.getAddressInfo(
            walletAddress = walletAddress,
            tokenAddress = tokenAddress
        )
    }

}