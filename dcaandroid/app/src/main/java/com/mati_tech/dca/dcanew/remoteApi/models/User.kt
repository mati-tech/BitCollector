package com.mati_tech.dca.dcanew.remoteApi.models
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class User(
    val id: Int? = null,
    val email: String,
    val password: String,
    val full_name: String,
    val role: String = "user",
    val registered_at: Long = System.currentTimeMillis()
)
