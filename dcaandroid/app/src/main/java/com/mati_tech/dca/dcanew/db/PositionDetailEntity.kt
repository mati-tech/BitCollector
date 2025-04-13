package com.mati_tech.dca.dcanew.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "position_details",
    foreignKeys = [
        ForeignKey(
            entity = PositionEntity::class,
            parentColumns = ["id"],
            childColumns = ["positionId"],
            onDelete = CASCADE
        )
    ]
)
data class PositionDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val positionId: Int,  // Links to PositionEntity (Main Position)
    val tradeType: String, // "BUY" or "SELL"
    val tradeAmount: Double,  // Amount of crypto bought/sold
    val tradePrice: Double,   // Price per coin during trade
    val totalCost: Double,    // Amount * Price (for BUY) | Amount * Price (for SELL)
    @ColumnInfo(defaultValue = "0.0")
    val availableUsd: Double = 0.0,  // New field – non-nullable, d
    val timestamp: Long = System.currentTimeMillis()
)
