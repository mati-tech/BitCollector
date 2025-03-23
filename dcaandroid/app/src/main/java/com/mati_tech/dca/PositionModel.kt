package com.mati_tech.dca

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class PositionModel(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("coin") val coin: String,
    @SerializedName("buyPrice")val buyPrice: Double,
    @SerializedName("amount")val amount: Double,
    @SerializedName("currentPrice") var currentPrice: Double = 0.0
)
