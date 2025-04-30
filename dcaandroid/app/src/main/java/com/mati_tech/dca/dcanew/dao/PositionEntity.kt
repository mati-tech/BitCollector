package com.mati_tech.dca.dcanew.dao


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "positions")
data class PositionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coin: String,
    val buyPrice: Double,
    val amount: Double,
    val valueUsd: Double,
    @ColumnInfo(defaultValue = "0")
    val hasDetails: Boolean = false,
    val sellPrice: Double? = null,
    val positionPnl: Double = 0.0,
    val buytimestamp: Long = System.currentTimeMillis(),
    val selltimestamp: Long? = null, // Change from default currentTimeMillis()
//    var currentPrice: Double = 0.0
)

