package com.tinnovakovic.ewallet.shared

import com.tinnovakovic.ewallet.shared.RateLimitHandlerImpl.Companion.AVOID_RATE_LIMIT_IN_MILLIS

interface RateLimitHandler {

    suspend fun delay(delayInMillis: Long = AVOID_RATE_LIMIT_IN_MILLIS)

}
