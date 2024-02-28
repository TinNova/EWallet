package com.tinnovakovic.ewallet.domain

import com.tinnovakovic.ewallet.data.EthplorerRepo
import javax.inject.Inject
import com.tinnovakovic.ewallet.data.Token as DataToken

class GetTopTokensUseCase @Inject constructor(
    private val ethplorerRepo: EthplorerRepo,
    private val topTokensInMemoryCache: TopTokensInMemoryCache
) {

    suspend fun execute(): List<Token> {
        return if (topTokensInMemoryCache.cache.value.isNotEmpty()) {
            topTokensInMemoryCache.cache.value
        } else {
            val dataTokens = ethplorerRepo.getTopTokens().tokens
            val domainTokens = mapDataTokenToDomainToken(dataTokens)
            topTokensInMemoryCache.updateCache(domainTokens)
            topTokensInMemoryCache.cache.value
        }
    }

    private fun mapDataTokenToDomainToken(dataTokens: List<DataToken>): List<Token> {
        return dataTokens.mapNotNull {
            val symbol = it.symbol ?: return@mapNotNull null
            Token(address = it.address, symbol = symbol)
        }
    }
}
