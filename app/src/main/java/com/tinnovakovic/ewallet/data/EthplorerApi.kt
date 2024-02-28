package com.tinnovakovic.ewallet.data

import com.tinnovakovic.ewallet.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EthplorerApi {

//    https://api.ethplorer.io/getTopTokens?limit=100&apiKey=freekey
//    https://api.ethplorer.io/getAddressInfo/0xd8dA6BF26964aF9D7eEd9e03E53415D37aA96045?token=0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2&showETHTotals=false&showTxsCount=false&apiKey=freekey

    @GET("getTopTokens")
    suspend fun getTopTokens(
        @Query("limit") limit: Int = 100,
        @Query("apiKey") apiKey: String = BuildConfig.ETHERSCAN_API_KEY //better to be in an interceptor?
    ): TopTokensData

    @GET("getAddressInfo/{wallet_address}")
    suspend fun getAddressInfo(
        @Path("wallet_address") walletAddress: String,
        @Query("token") tokenAddress: String,
        @Query("showETHTotals") showETHTotals: Boolean = false,
        @Query("showTxsCount") showTxsCount: Boolean = false,
        @Query("apiKey") apiKey: String = BuildConfig.ETHERSCAN_API_KEY //better to be in an interceptor?
    ): AddressInfoData

}