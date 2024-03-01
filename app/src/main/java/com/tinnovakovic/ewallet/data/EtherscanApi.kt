package com.tinnovakovic.ewallet.data

import com.tinnovakovic.ewallet.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query


interface EtherscanApi {

    @GET("api")
    suspend fun getLatestTokenBalance(
        @Query("module") module: String = "account",
        @Query("action") action: String = "tokenbalance",
        @Query("tag") tag: String = "latest",
        @Query("contractaddress") tokenAddress: String,
        @Query("address") walletAddress: String,
        @Query("apiKey") apiKey: String = BuildConfig.ETHERSCAN_API_KEY //better to be in an interceptor?
    ): Result<TokenBalanceData>

}