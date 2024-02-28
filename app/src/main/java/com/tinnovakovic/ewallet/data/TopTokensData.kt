package com.tinnovakovic.ewallet.data

data class TopTokensData(
    val tokens: List<Token>
)

data class Token(
    val address: String,
    val name: String,
    val symbol: String,
)
