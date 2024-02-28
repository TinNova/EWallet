package com.tinnovakovic.ewallet.domain

import java.util.Locale
import javax.inject.Inject

class FilterListOfTokensUseCase @Inject constructor(
    private val getTopTokensUseCase: GetTopTokensUseCase
) {

    suspend fun execute(searchText: String): List<Token> {
        val tokens = getTopTokensUseCase.execute()
        return tokens.filter {
            it.symbol.startsWith(searchText.uppercase(Locale.getDefault()))
        }
    }
}
