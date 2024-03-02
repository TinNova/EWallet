package com.tinnovakovic.ewallet.shared

import com.tinnovakovic.ewallet.data.TokenBalanceData
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class EtherscanResultCallTest {

    private val delegate: Call<TokenBalanceData> = mockk(relaxed = true)

    private val sut: EtherscanResultCall = EtherscanResultCall(
        delegate
    )

    @Test
    fun `GIVEN a successful response with status=1, WHEN enqueue(), THEN verify a successful response was captured`() {
        // GIVEN
        val mockCallback: Callback<Result<TokenBalanceData>> = mockk(relaxed = true)
        val tokenBalanceData = TokenBalanceData(status = "1", message = "message", result = "result")
        val response = Response.success(tokenBalanceData)

        val slot = slot<Callback<TokenBalanceData>>()
        every { delegate.enqueue(capture(slot)) } answers {
            slot.captured.onResponse(delegate, response)
        }

        // WHEN
        sut.enqueue(mockCallback)

        // THEN
        verify {
            mockCallback.onResponse(
                eq(sut),
                match {
                    it.isSuccessful && it.body()?.getOrNull()?.result == "result"
                }
            )
        }
    }

    @Test
    fun `GIVEN a successful response with status=0 and result=RateLimitException, WHEN enqueue(), THEN verify a successful response was captured containing the RateLimitException object`() {
        // GIVEN
        val mockCallback: Callback<Result<TokenBalanceData>> = mockk(relaxed = true)
        val tokenBalanceData = TokenBalanceData(status = "0", message = "message", result = EtherscanResultCall.RATE_LIMIT_REACHED)
        val response = Response.success(tokenBalanceData)

        val slot = slot<Callback<TokenBalanceData>>()

        every { delegate.enqueue(capture(slot)) } answers {
            slot.captured.onResponse(delegate, response)
        }

        // WHEN
        sut.enqueue(mockCallback)

        // THEN
        verify(exactly = 1) {
            mockCallback.onResponse(
                eq(sut),
                match { response ->
                    val result = response.body()
                    result != null && result.isFailure && result.exceptionOrNull() is EtherScanApiKeyException.RateLimitException
                }
            )
        }
    }

    @Test
    fun `GIVEN a failed response with IOException, WHEN enqueue(), THEN verify a failed response was captured containing the IOException object`() {
        // GIVEN
        val mockCallback: Callback<Result<TokenBalanceData>> = mockk(relaxed = true)
        val ioException = IOException("Network error")

        val slot = slot<Callback<TokenBalanceData>>()
        every { delegate.enqueue(capture(slot)) } answers {
            slot.captured.onFailure(delegate, ioException)
        }

        // WHEN
        sut.enqueue(mockCallback)

        // THEN
        verify(exactly = 1) {
            mockCallback.onResponse(
                eq(sut),
                match { response ->
                    val result = response.body()
                    result != null && result.isFailure && result.exceptionOrNull()?.cause is IOException
                }
            )
        }
    }

    // In a production application all of the scenarios would be tested.

}
