package com.mati_tech.dca.dcanew.remoteApi.client

import com.mati_tech.dca.dcanew.remoteApi.services.PositionApiService
import com.mati_tech.dca.dcanew.remoteApi.services.PositionDetailApiService
import com.mati_tech.dca.dcanew.remoteApi.services.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://bitcollector.onrender.com/"
//    private const val BASE_URL = "https://store/ly-app-latest.onrender.com/"

//    private const val BASE_URL = "http://10.0.2.2:8080"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    // Create an instance of ApiService

    val userApiService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
    val positionApiService: PositionApiService by lazy {
        retrofit.create(PositionApiService::class.java)
    }
    val positionDetailApiService: PositionDetailApiService by lazy {
        retrofit.create(PositionDetailApiService::class.java)
    }
}