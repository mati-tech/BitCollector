package com.mati_tech.dca.dcanew.remoteApi.services
import com.mati_tech.dca.dcanew.remoteApi.models.ApiResponse
import com.mati_tech.dca.dcanew.remoteApi.models.Position
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
interface PositionApiService {

    @GET("/positions/byUserId/{userId}")
    suspend fun getPositionsByUserId(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<List<Position>>

    @GET("/positions/byId/{positionId}")
    suspend fun getPositionById(
        @Header("Authorization") token: String,
        @Path("positionId") positionId: Int
    ): Response<Position>

    @POST("/positions")
    suspend fun createPosition(
        @Header("Authorization") token: String,
        @Body position: Position
    ): Response<ApiResponse>

    @PUT("/positions/{positionId}")
    suspend fun updatePosition(
        @Header("Authorization") token: String,
        @Path("positionId") positionId: Int,
        @Body position: Position
    ): Response<String>

    @DELETE("/positions/{positionId}")
    suspend fun deletePosition(
        @Header("Authorization") token: String,
        @Path("positionId") positionId: Int
    ): Response<Unit>
}