package com.tinnovakovic.ewallet.shared

import com.tinnovakovic.ewallet.R
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ExceptionHandlerImpl @Inject constructor(
    private val contextProvider: ContextProvider
) : ExceptionHandler {

    private val context = contextProvider.getContext()

    // Documentation for the api errors we can get
    // https://info.etherscan.com/api-return-errors/
    // https://github.com/EverexIO/Ethplorer/wiki/Ethplorer-API#get-top-tokens

    override fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is IOException -> context.getString(R.string.io_error_message)

            is EtherScanApiKeyException.InvalidApiKeyException,
            is EtherScanApiKeyException.InvalidApiKeyRateLimitException -> {
                context.getString(R.string.http_authentication_error_message)
            }

            is EtherScanApiKeyException.RateLimitException -> {
                context.getString(R.string.rate_limit_error_message)
            }

            is EthplorerErrorException -> {
                when (throwable.httpStatusCode) {
                    401, 402 -> context.getString(R.string.http_authentication_error_message)
                    400, 404, 406 -> context.getString(R.string.http_invalid_data_error_message)
                    503 -> context.getString(R.string.server_error_message)
                    else -> context.getString(R.string.unknown_message)
                }
            }

            is HttpException -> {
                when (throwable.code()) {
                    in 400..499 -> context.getString(R.string.generic_four_hundred_error_message)
                    in 500..599 -> context.getString(R.string.generic_five_hundred_error_message)
                    else -> context.getString(R.string.unknown_message)
                }
            }

            else -> context.getString(R.string.unknown_message)
        }
    }
}
