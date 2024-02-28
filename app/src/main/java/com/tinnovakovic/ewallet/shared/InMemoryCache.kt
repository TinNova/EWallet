package com.tinnovakovic.ewallet.shared

import kotlinx.coroutines.flow.StateFlow

interface InMemoryCache<T> {

    val cache: StateFlow<T?>

    suspend fun updateCache(newData: T)
}