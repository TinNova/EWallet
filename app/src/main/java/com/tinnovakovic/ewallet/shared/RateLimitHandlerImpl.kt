package com.tinnovakovic.ewallet.shared

import kotlinx.coroutines.delay
import javax.inject.Inject

class RateLimitHandlerImpl @Inject constructor() : RateLimitHandler {
    override suspend fun rateLimitDelay(delayInMillis: Long) {
        delay(delayInMillis)
    }

    companion object {
        const val AVOID_RATE_LIMIT_IN_MILLIS = 200L // allows for a maximum of 5 calls per second
    }

}