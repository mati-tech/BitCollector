package com.mati_tech.dca.dcanew.db

import androidx.room.*

@Dao
interface PositionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosition(position: PositionEntity)

    @Query("SELECT * FROM positions WHERE sellPrice IS NULL ORDER BY buytimestamp DESC")
    suspend fun getAllPositions(): List<PositionEntity>

    @Query("SELECT * FROM positions WHERE sellPrice IS Not NULL ORDER BY buytimestamp DESC")
    suspend fun getAllPositionsHistory(): List<PositionEntity>

    @Query("SELECT * FROM positions WHERE sellPrice IS Not NULL and positionPnl > 0 ORDER BY buytimestamp DESC")
    suspend fun getAllPositionsHistoryPlusPNL(): List<PositionEntity>

    @Query("SELECT * FROM positions WHERE sellPrice IS Not NULL and positionPnl < 0 ORDER BY buytimestamp DESC")
    suspend fun getAllPositionsHistoryNegativePNL(): List<PositionEntity>

    @Query("DELETE FROM positions")
    suspend fun deleteAllPositions()
    // New method to close a position by updating sellPrice and selltimestamp

    @Delete
    suspend fun deletePosition(position: PositionEntity)

    @Query("UPDATE positions SET sellPrice = :sellPrice, positionPnl = :pnl, selltimestamp = :sellTimestamp WHERE id = :positionId")
    suspend fun closePosition(
        positionId: Int,
        sellPrice: Double?,
        pnl: Double?,
        sellTimestamp: Long = System.currentTimeMillis()
    )
//    suspend fun updatePosition(id: Int, newPrice: Double, newAmount: Double, newTotalUsd: Double)
// ✅ Corrected update method
    @Query("UPDATE positions SET buyPrice = :newPrice, amount = :newAmount, valueUsd = :newTotalUsd WHERE id = :id")
    suspend fun updatePosition(
        id: Int,
        newPrice: Double,
        newAmount: Double,
        newTotalUsd: Double
    )
    @Query("SELECT SUM(valueUsd) FROM positions where sellPrice IS NULL")
    suspend fun getTotalInPositions(): Double?


    // here to update the price back in the table after trading in the position detail
    @Query("UPDATE positions SET amount = :newAmount, buyPrice = :newAvgPrice WHERE id = :positionId")
    suspend fun updatePositionStats(positionId: Int, newAmount: Double, newAvgPrice: Double)
}
