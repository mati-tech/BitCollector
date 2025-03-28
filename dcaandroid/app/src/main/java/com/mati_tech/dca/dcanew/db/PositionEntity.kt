package com.mati_tech.dca.dcanew.db


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "positions")
data class PositionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coin: String,
    val buyPrice: Double,
    val amount: Double,
    val valueUsd: Double,
    val sellPrice: Double? = null,
    val buytimestamp: Long = System.currentTimeMillis(),
    val selltimestamp: Long? = null, // Change from default currentTimeMillis()
    var currentPrice: Double = 0.0
)

