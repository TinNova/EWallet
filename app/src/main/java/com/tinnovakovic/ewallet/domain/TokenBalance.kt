package com.tinnovakovic.ewallet.domain

data class TokenBalance(
    val symbol: String,
    val tokenAddress: String,
    val balance: String,
)
