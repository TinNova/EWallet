package com.tinnovakovic.ewallet.data

sealed class EthplorerModel {
    data class TopTokensData(
        val tokens: List<Token>
    ) : EthplorerModel()

    data class ErrorData(
        val error: Error
    ) : EthplorerModel()
}

data class Token(
    val address: String,
    val symbol: String?,
    val decimals: String,
)

data class Error(
    val code: Int,
    val message: String
)
