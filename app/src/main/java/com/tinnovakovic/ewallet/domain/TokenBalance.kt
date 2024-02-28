package com.tinnovakovic.ewallet.domain

data class TokenBalance(
    val symbol: String,
    val result: String,
    val isResultZero: Boolean
)