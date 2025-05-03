package com.mati_tech.dca.dcanew.remoteApi.models

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequest(
    val email: String,
    val password: String
)
