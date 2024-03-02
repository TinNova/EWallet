package com.tinnovakovic.ewallet.data

import com.tinnovakovic.ewallet.shared.InMemoryCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkInMemoryCache @Inject constructor() : InMemoryCache<Boolean> {

    private val _cache = MutableStateFlow<Boolean>(false)
    override val cache = _cache.asStateFlow()
    override suspend fun updateCache(newData: Boolean) {
        _cache.emit(newData)

    }
}