package com.mati_tech.dca.dcanew.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {
    @GET("api/v3/ticker/price")
    suspend fun getCoinPrices(): List<CryptoResponse>
}

data class CryptoResponse(
    val symbol: String,
    val price: String
)
