package com.mati_tech.dca.dcanew.remoteApi.repos

import com.mati_tech.dca.dcanew.remoteApi.models.ApiResponse
import com.mati_tech.dca.dcanew.remoteApi.models.User
import com.mati_tech.dca.dcanew.remoteApi.models.UserLoginRequest
import com.mati_tech.dca.dcanew.remoteApi.services.UserApiService
import retrofit2.HttpException
import retrofit2.Response
class UserRepos(private val api: UserApiService){
    suspend fun register(user: User): Response<ApiResponse> {
        return api.registerUser(user)
    }

    suspend fun login(request: UserLoginRequest): Response<Map<String, String>> {
        return api.loginUser(request)
    }

    suspend fun getAllUsers(token: String): Response<List<User>> {
        return api.getAllUsers("Bearer $token")
    }

    suspend fun getUserById(token: String, userId: Int): Response<User> {
        return api.getUserById("Bearer $token", userId)
    }

    suspend fun getUserByEmail(token: String, email: String): Response<User> {
        return api.getUserByEmail("Bearer $token", email)
    }

    suspend fun updateUser(token: String, userId: Int, user: User): Response<String> {
        return api.updateUser("Bearer $token", userId, user)
    }

    suspend fun deleteUser(token: String, userId: Int): Response<Unit> {
        return api.deleteUser("Bearer $token", userId)
    }
}