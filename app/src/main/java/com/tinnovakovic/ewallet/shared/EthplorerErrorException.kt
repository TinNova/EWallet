package com.tinnovakovic.ewallet.shared

sealed class EthplorerErrorException : Throwable() {
    abstract val code: Int
    abstract val errorMessage: String
    abstract val httpStatusCode: Int
    data class AuthenticationException(
        override val code: Int,
        override val errorMessage: String,
        override val httpStatusCode: Int
    ) : EthplorerErrorException()

    data class SystemErrorsException(
        override val code: Int,
        override val errorMessage: String,
        override val httpStatusCode: Int
    ) : EthplorerErrorException()

    data class InvalidParameterErrorsException(
        override val code: Int,
        override val errorMessage: String,
        override val httpStatusCode: Int
    ) : EthplorerErrorException()

    data class UnknownException(
        override val code: Int,
        override val errorMessage: String,
        override val httpStatusCode: Int
    ) : EthplorerErrorException()
}
