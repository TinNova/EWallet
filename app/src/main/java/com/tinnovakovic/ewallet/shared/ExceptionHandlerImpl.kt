package com.tinnovakovic.ewallet.shared

import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ExceptionHandlerImpl @Inject constructor() : ExceptionHandler {

    override fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    //TODO Tidy this up according to the real errors we can get
                    // It might require a custom CallAdapter just like with Etherscan Api
                    // https://github.com/EverexIO/Ethplorer/wiki/Ethplorer-API#get-top-tokens
                    401 -> "Authentication Error, Please try again." //Invalid API Key
                    429 -> "Rate limit exceeded. Please try again later."
                    404 -> "Resource not found."
                    500 -> "Internal server error."
                    else -> "Unknown error occurred."
                }
            }

            is IOException -> "No internet, we've cleared the search bar, please search again.."
            is EtherScanApiKeyException -> {
                // https://info.etherscan.com/api-return-errors/
                throwable.result
            }

            else -> throwable.message ?: "Unknown Error, Please Try Again."
        }
    }
}
