package com.tinnovakovic.ewallet.data

import javax.inject.Inject

class EtherscanRepo @Inject constructor(
    private val etherscanApi: EtherscanApi
) {

    suspend fun getLatestTokenBalance(walletAddress: String, tokenAddress: String): TokenBalanceData {
        return etherscanApi
            .getLatestTokenBalance(
                walletAddress = walletAddress,
                tokenAddress = tokenAddress
            )
    }
}
