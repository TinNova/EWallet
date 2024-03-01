package com.tinnovakovic.ewallet.shared

import kotlinx.coroutines.delay
import javax.inject.Inject

class RetryIoImpl @Inject constructor() : RetryIo {

    override suspend fun <T> retryIO(
        attempts: Int,
        initialDelay: Long,
        maxDelay: Long,
        factor: Double,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(attempts - 1) {
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block() // last attempt
    }
}