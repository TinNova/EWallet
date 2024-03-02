package com.tinnovakovic.ewallet.data

import com.tinnovakovic.ewallet.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface EthplorerApi {


    @GET("getTopTokens")
    suspend fun getTopTokens(
        @Query("limit") limit: Int = 100,
        @Query("apiKey") apiKey: String = BuildConfig.ETHPLORER_API_KEY //better to be in an interceptor?
    ): Result<EthplorerModel.TopTokensData>

}
