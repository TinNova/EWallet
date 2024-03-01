package com.tinnovakovic.ewallet.shared

import com.tinnovakovic.ewallet.data.TokenBalanceData
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.RuntimeException

data class EtherScanApiKeyException(
    val status: String,
    val result: String
) : Throwable()

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
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val status = responseBody.status

                            if (status == "1") {
                                // Successful response
                                callback.onResponse(
                                    this@EtherscanResultCall,
                                    Response.success(Result.success(response.body()!!))
                                )
                            } else {
                                callback.onResponse(
                                    this@EtherscanResultCall,
                                    Response.success(
                                        Result.failure(
                                            EtherScanApiKeyException(
                                                status = response.body()!!.result,
                                                result = response.body()!!.result
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
                        is EtherScanApiKeyException -> "Api Key Error" //TODO: Handle this better!!
                        is IOException -> "No internet connection"
                        is HttpException -> "Something went wrong!"
                        else -> t.localizedMessage
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
        return ResultCall(delegate.clone())
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }
}
