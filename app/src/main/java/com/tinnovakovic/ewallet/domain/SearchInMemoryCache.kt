package com.tinnovakovic.ewallet.domain

import com.tinnovakovic.ewallet.domain.GetTokensWithBalancesUseCase.SearchCache
import com.tinnovakovic.ewallet.shared.InMemoryCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SearchInMemoryCache @Inject constructor(): InMemoryCache<SearchCache> {

    private val _cache = MutableStateFlow<SearchCache>(SearchCache())
    override val cache = _cache.asStateFlow()
    override suspend fun updateCache(newData: SearchCache) {
        _cache.emit(newData)

    }

}
