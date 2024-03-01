package com.tinnovakovic.ewallet.shared

sealed class EtherScanApiKeyException : Throwable() {
    abstract val status: String
    abstract val result: String

    data class RateLimitException(
        override val status: String,
        override val result: String
    ) : EtherScanApiKeyException()

    data class InvalidApiKeyException(
        override val status: String,
        override val result: String
    ) : EtherScanApiKeyException()

    data class InvalidApiKeyRateLimitException(
        override val status: String,
        override val result: String
    ) : EtherScanApiKeyException()
}
