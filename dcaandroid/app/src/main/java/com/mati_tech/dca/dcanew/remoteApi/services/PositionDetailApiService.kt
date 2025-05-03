package com.mati_tech.dca.dcanew.remoteApi.services


import com.mati_tech.dca.dcanew.remoteApi.models.PositionDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PositionDetailApiService {

    @GET("/positionDetails/byPositionId/{positionId}")
    suspend fun getDetailsByPositionId(
        @Header("Authorization") token: String,
        @Path("positionId") positionId: Int
    ): Response<List<PositionDetail>>

    @GET("/positionDetails/byId/{detailId}")
    suspend fun getDetailById(
        @Header("Authorization") token: String,
        @Path("detailId") detailId: Int
    ): Response<PositionDetail>

    @POST("/positionDetails")
    suspend fun createPositionDetail(
        @Header("Authorization") token: String,
        @Body positionDetail: PositionDetail
    ): Response<PositionDetail>

    @PUT("/positionDetails/{detailId}")
    suspend fun updatePositionDetail(
        @Header("Authorization") token: String,
        @Path("detailId") detailId: Int,
        @Body positionDetail: PositionDetail
    ): Response<String>

    @DELETE("/positionDetails/{detailId}")
    suspend fun deletePositionDetail(
        @Header("Authorization") token: String,
        @Path("detailId") detailId: Int
    ): Response<Unit>
}