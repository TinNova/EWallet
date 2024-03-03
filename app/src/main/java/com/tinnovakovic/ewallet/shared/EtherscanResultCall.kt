package com.tinnovakovic.ewallet.shared

import android.util.Log
import com.tinnovakovic.ewallet.data.TokenBalanceData
import com.tinnovakovic.ewallet.shared.EtherScanApiKeyException.InvalidApiKeyException
import com.tinnovakovic.ewallet.shared.EtherScanApiKeyException.InvalidApiKeyRateLimitException
import com.tinnovakovic.ewallet.shared.EtherScanApiKeyException.RateLimitException
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.RuntimeException

class EtherscanResultCall(val delegate: Call<TokenBalanceData>) :
    Call<Result<TokenBalanceData>> {

    override fun enqueue(callback: Callback<Result<TokenBalanceData>>) {
        delegate.enqueue(
            object : Callback<TokenBalanceData> {
                override fun onResponse(
                    call: Call<TokenBalanceData>,
                    response: Response<TokenBalanceData>
                ) {

                    if (response.isSuccessful) {
//                         to test bad response
//                        val responseBody: TokenBalanceData? = TokenBalanceData(status="0", message="NOTOK", result="Max rate limit reached")
                        val responseBody: TokenBalanceData? = response.body()
                        if (responseBody != null) {
                            val status = responseBody.status

                            if (status == "1") {
                                // Successful response
                                callback.onResponse(
                                    this@EtherscanResultCall,
                                    Response.success(Result.success(responseBody))
                                )
                            } else {
                                callback.onResponse(
                                    this@EtherscanResultCall,
                                    Response.success(
                                        Result.failure(
                                            etherScanApiKeyExceptionHandler(
                                                responseBody.status,
                                                responseBody.result
                                            )
                                        )
                                    )
                                )
                            }
                        }
                    } else {
                        callback.onResponse(
                            this@EtherscanResultCall,
                            Response.success(
                                Result.failure(
                                    HttpException(response)
                                )
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<TokenBalanceData>, t: Throwable) {
                    val errorMessage = when (t) {
                        is IOException -> t.message
                        is HttpException -> t.message
                        else -> t.message
                    }
                    callback.onResponse(
                        this@EtherscanResultCall,
                        Response.success(Result.failure(RuntimeException(errorMessage, t)))
                    )
                }
            }
        )
    }

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun execute(): Response<Result<TokenBalanceData>> {
        return Response.success(Result.success(delegate.execute().body()!!))
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun clone(): Call<Result<TokenBalanceData>> {
        return EtherscanResultCall(delegate.clone())
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }

    private fun etherScanApiKeyExceptionHandler(
        status: String,
        result: String
    ): EtherScanApiKeyException {

        return when (result) {
            RATE_LIMIT_REACHED -> {
                RateLimitException(
                    status = status,
                    result = RATE_LIMIT_REACHED
                )
            }

            INVALID_API_KEY -> {
                InvalidApiKeyException(
                    status = status,
                    result = INVALID_API_KEY
                )
            }

            else -> {
                InvalidApiKeyRateLimitException(
                    status = status,
                    result = INVALID_API_KEY_RATE_LIMIT_REACHED
                )
            }
        }
    }

    companion object {
        const val RATE_LIMIT_REACHED = "Max rate limit reached"
        const val INVALID_API_KEY = "Invalid API Key"
        const val INVALID_API_KEY_RATE_LIMIT_REACHED =
            "Too many invalid api key attempts, please try again later"
    }
}
