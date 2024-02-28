package com.tinnovakovic.ewallet.domain

import com.tinnovakovic.ewallet.shared.InMemoryCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopTokensInMemoryCache @Inject constructor() : InMemoryCache<List<Token>> {

    private val _cache = MutableStateFlow<List<Token>>(emptyList())
    override val cache = _cache.asStateFlow()
    override suspend fun updateCache(newData: List<Token>) {
        _cache.emit(newData)

    }
}
