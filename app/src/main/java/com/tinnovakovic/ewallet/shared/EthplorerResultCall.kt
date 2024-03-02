package com.tinnovakovic.ewallet.shared

import com.tinnovakovic.ewallet.data.EthplorerModel
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.RuntimeException

class EthplorerResultCall(val delegate: Call<EthplorerModel>) :
    Call<Result<EthplorerModel>> {

    override fun enqueue(callback: Callback<Result<EthplorerModel>>) {
        delegate.enqueue(
            object : Callback<EthplorerModel> {
                override fun onResponse(
                    call: Call<EthplorerModel>,
                    response: Response<EthplorerModel>
                ) {
                    if (response.isSuccessful) {

                        val responseBody: EthplorerModel? = when (response.body()) {
                            is EthplorerModel.TopTokensData -> response.body()
                            is EthplorerModel.ErrorData -> response.body()
                            null -> null
                        }

                        if (responseBody != null) {
                            when (responseBody) {
                                is EthplorerModel.TopTokensData -> {
                                    callback.onResponse(
                                        this@EthplorerResultCall,
                                        Response.success(Result.success(responseBody))
                                    )
                                }

                                is EthplorerModel.ErrorData -> {
                                    //handle it as failure
                                    callback.onResponse(
                                        this@EthplorerResultCall,
                                        Response.success(
                                            Result.failure(
                                                errorDataExceptionHandler(
                                                    responseBody.error.code,
                                                    responseBody.error.message
                                                )
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<EthplorerModel>, t: Throwable) {
                    val errorMessage = when (t) {
                        is IOException -> t.message
                        is HttpException -> t.message()
                        else -> t.message
                    }
                    callback.onResponse(
                        this@EthplorerResultCall,
                        Response.success(Result.failure(RuntimeException(errorMessage, t)))
                    )
                }
            }
        )
    }

    private fun errorDataExceptionHandler(code: Int, message: String): EthplorerErrorException {

        return when (code) {
            1, 133, 135, 136 -> {
                EthplorerErrorException.AuthenticationException(
                    code = code,
                    errorMessage = message,
                    httpStatusCode = if (code == 1) 401 else 403
                )
            }

            3, 999 -> {
                EthplorerErrorException.SystemErrorsException(
                    code = code,
                    errorMessage = message,
                    httpStatusCode = 503
                )
            }

            101, 102, 104, 108, 111, 150, 404 -> {
                EthplorerErrorException.InvalidParameterErrorsException(
                    code = code,
                    errorMessage = message,
                    httpStatusCode = when (code) {
                        101, 102, 111, 150 -> 400
                        104, 108 -> 406
                        else -> 404
                    }
                )
            }

            else -> {
                EthplorerErrorException.UnknownException(
                    code = code,
                    errorMessage = message,
                    httpStatusCode = -1
                )
            }
        }
    }


    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun execute(): Response<Result<EthplorerModel>> {
        return Response.success(Result.success(delegate.execute().body()!!))
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun clone(): Call<Result<EthplorerModel>> {
        return EthplorerResultCall(delegate.clone())
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }
}
