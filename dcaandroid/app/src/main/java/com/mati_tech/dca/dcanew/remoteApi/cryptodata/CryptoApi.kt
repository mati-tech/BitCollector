package com.mati_tech.dca.dcanew.remoteApi.cryptodata

import retrofit2.http.GET

interface CryptoApi {
    @GET("api/v3/ticker/price")
    suspend fun getCoinPrices(): List<CryptoResponse>
}

data class CryptoResponse(
    val symbol: String,
    val price: String
)
