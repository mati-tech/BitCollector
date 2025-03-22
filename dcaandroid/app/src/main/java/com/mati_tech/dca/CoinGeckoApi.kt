package com.mati_tech.dca

import retrofit2.http.GET
import retrofit2.Call

interface CoinGeckoApi {
    @GET("simple/price?ids=litecoin&vs_currencies=usd")
    fun getLtcPrice(): Call<CoinPriceResponse>
}

data class CoinPriceResponse(
    val litecoin: CoinDetails
)

data class CoinDetails(
    val usd: Double
)
