package com.tinnovakovic.ewallet.domain

import com.tinnovakovic.ewallet.domain.GetTokensWithBalancesUseCase.SearchCache
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GetTokensWithBalancesUseCaseTest {

    private val getTopTokensUseCase: GetTopTokensUseCase = mockk(relaxed = true)
    private val getLatestTokenBalancesUseCase: GetLatestTokenBalancesUseCase = mockk(relaxed = true)
    private val searchInMemoryCache: SearchInMemoryCache = mockk(relaxed = true)

    private val sut: GetTokensWithBalancesUseCase = GetTokensWithBalancesUseCase(
        getTopTokensUseCase,
        getLatestTokenBalancesUseCase,
        searchInMemoryCache
    )

    @Test
    fun `GIVEN searchText is empty, searchCache is contains data, WHEN execute(), THEN return an emptyList`() =
        runTest {
            //GIVEN
            val searchText = ""
            val savedTokenBalance = mockk<TokenBalance>()
            val searchCache = SearchCache("a", mutableListOf(savedTokenBalance))
            every { searchInMemoryCache.cache.value } returns searchCache

            val expected = emptyList<TokenBalance>()

            //WHEN
            val result = sut.execute(searchText)

            //THEN
            assertEquals(expected, result)
        }

    @Test
    fun `GIVEN searchText is FT, savedSearchText is F and savedTokenBalances contains 4 TokenBalances, WHEN execute(), THEN return filtered tokenBalances and set savedSearch to first letter of searchText`() =
        runTest {
            //GIVEN
            val searchText = "FT"
            val searchCache = SearchCache(
                "F",
                mutableListOf(
                    TokenBalance(symbol = "FTSE", result = "result", false),
                    TokenBalance(symbol = "FTSEE", result = "result", false),
                    TokenBalance(symbol = "FTSEEE", result = "result", false),
                    TokenBalance(symbol = "FIND", result = "result", false)
                )
            )

            every { searchInMemoryCache.cache.value } returns searchCache

            val expected = listOf(
                TokenBalance(symbol = "FTSE", result = "result", false),
                TokenBalance(symbol = "FTSEE", result = "result", false),
                TokenBalance(symbol = "FTSEEE", result = "result", false),
            )

            //WHEN
            val result = sut.execute(searchText)

            //THEN
            assertEquals(expected, result)
            assertEquals(searchCache.savedSearchStartedWith, searchText.first().toString())
        }

    @Test
    fun `GIVEN searchText is FT, savedSearchText is F and savedTokenBalances is empty WHEN execute(), THEN fetch tokens and tokenBalances and return a new tokenBalances list`() =
        runTest {
            //GIVEN
            val searchText = "FT"
            val searchCache = SearchCache(
                "F", mutableListOf()
            )

            val tokens = listOf(
                Token(symbol = "FTSE", address = "", decimals = ""),
                Token(symbol = "FTSEE", address = "", decimals = ""),
                Token(symbol = "APPLE", address = "", decimals = ""),
                Token(symbol = "PEAR", address = "", decimals = ""),
            )

            val expected = listOf(
                TokenBalance(symbol = "FTSE", result = "", false),
                TokenBalance(symbol = "FTSEE", result = "", false)
            )

            every { searchInMemoryCache.cache.value } returns searchCache
            coEvery { getTopTokensUseCase.execute() } returns tokens
            coEvery { getLatestTokenBalancesUseCase.execute(tokens.take(2)) } returns expected

            //WHEN
            val result = sut.execute(searchText)

            //THEN
            assertEquals(expected, result)
            assertEquals(searchCache.savedSearchStartedWith, searchText.first().toString())
            assertEquals(searchCache.savedTokenBalances, result)
        }

    @Test
    fun `GIVEN savedSearch is blank, getTopTokens returns two  matching results of four, WHEN execute(), THEN return() two tokenBalances and set searchText to savedSearch and add tokens to savedTokens`() =
        runTest {
            //GIVEN
            val searchText = "F"
            val searchCache = SearchCache("", mutableListOf())

            val tokens = listOf(
                Token(symbol = "FTSE", address = "", decimals = ""),
                Token(symbol = "FTSEE", address = "", decimals = ""),
                Token(symbol = "APPLE", address = "", decimals = ""),
                Token(symbol = "PEAR", address = "", decimals = ""),
            )

            val expected = listOf(
                TokenBalance(symbol = "FTSE", result = "", false),
                TokenBalance(symbol = "FTSEE", result = "", false)
            )

            every { searchInMemoryCache.cache.value } returns searchCache
            coEvery { getTopTokensUseCase.execute() } returns tokens
            coEvery { getLatestTokenBalancesUseCase.execute(tokens.take(2)) } returns expected

            //WHEN
            val result = sut.execute(searchText)

            //THEN
            assertEquals(expected, result)
            assertEquals(searchCache.savedSearchStartedWith, searchText.first().toString())
            assertEquals(searchCache.savedTokenBalances, result)

        }
}
