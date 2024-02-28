package com.tinnovakovic.ewallet.domain

import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject

class FromSmallestDecimalRepresentationUseCase @Inject constructor() {

    fun execute(smallestBalance: BigInteger, decimalPlaces: Int): String {
        // Convert smallestBalance to BigDecimal
        val balanceDecimal = BigDecimal(smallestBalance)

        // Divide the balance by 10^decimalPlaces
        val scaledBalance = balanceDecimal.divide(BigDecimal.TEN.pow(decimalPlaces))

        // Get the integer part
        val integerPart = scaledBalance.toBigInteger()

        // Get the decimal part by subtracting the integer part
        val remainder =
            balanceDecimal - (BigDecimal(integerPart) * BigDecimal.TEN.pow(decimalPlaces))

        // Combine the integer and decimal parts and return as string
        return "$integerPart.$remainder".removeTrailingZeros()
    }

    private fun String.removeTrailingZeros(): String {
        var result = this
        while (result.endsWith("0")) {
            result = result.dropLast(1)
        }
        if (result.endsWith(".")) {
            result = result.dropLast(1)
        }
        return result
    }
}
