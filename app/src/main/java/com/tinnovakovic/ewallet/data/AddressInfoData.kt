package com.tinnovakovic.ewallet.data

import com.google.gson.annotations.SerializedName

data class AddressInfoData(
    val address: String,
    @SerializedName("ETH")
    val eTH: Eth,
    val tokens: List<Token>
)

data class Eth(
    val balance: String?
)