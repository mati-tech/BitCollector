package com.mati_tech.dca.dcanew.remoteApi.services
import com.mati_tech.dca.dcanew.remoteApi.models.ApiResponse
import com.mati_tech.dca.dcanew.remoteApi.models.User
import com.mati_tech.dca.dcanew.remoteApi.models.UserLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
interface UserApiService {
    @POST("/register")
    suspend fun registerUser(@Body user: User): Response<ApiResponse>

    @POST("/login")
    suspend fun loginUser(@Body request: UserLoginRequest): Response<Map<String, String>>

    @GET("/users")
    suspend fun getAllUsers(@Header("Authorization") token: String): Response<List<User>>

    @GET("/users/byId/{id}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Response<User>

    @GET("/users/byEmail/{email}")
    suspend fun getUserByEmail(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Response<User>

    @PUT("/users/{id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") userId: Int,
        @Body user: User
    ): Response<String>

    @DELETE("/users/{id}")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Response<Unit>
}