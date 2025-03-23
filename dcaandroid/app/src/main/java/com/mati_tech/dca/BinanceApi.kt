package com.mati_tech.dca

import retrofit2.http.GET
import retrofit2.Call

import retrofit2.http.Query


//interface CoinGeckoApi {
//    @GET("simple/price")
//    fun getCoinPrice(
//        @Query("litecoin") coinId: String,
//        @Query("usd") currency: String
//    ): Call<Map<String, Map<String, Float>>>
//}

//interface CoinGeckoApi {
//    @GET("simple/price")
//    fun getCoinPrice(
//        @Query("ids") coinId: String, // ids is the correct parameter for coin IDs
//        @Query("vs_currencies") currency: String // vs_currencies is the correct parameter for the currency
//    ): Call<Map<String, Map<String, Float>>>
//}

//interface BinanceApi {
//    @GET("api1/v3/ticker/price")
//    fun getCoinPrice(@Query("symbol") symbol: String): Call<BinanceResponse>
//}
interface BinanceApi {
    @GET("api/v3/ticker/price")
    fun getCoinPrice(@Query("symbol") symbol: String): Call<Map<String, String>>
}

data class BinanceResponse(
    val symbol: String,
    val price: String
)


//interface CoinGeckoApi {
//    @GET("simple/price?ids=litecoin&vs_currencies=usd")
//    fun getLtcPrice(): Call<CoinPriceResponse>
//}
//
//data class CoinPriceResponse(
//    val litecoin: CoinDetails
//)
//
//data class CoinDetails(
//    val usd: Double
//)
