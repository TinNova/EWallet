package com.tinnovakovic.ewallet.shared

interface RetryIo {

    suspend fun <T> retryIO(
        attempts: Int = 3,
        initialDelay: Long = 400, // 0.4 seconds
        maxDelay: Long = 1600,    // 1.6 seconds
        factor: Double = 2.0,
        block: suspend () -> T
    ): T

}
