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

    @Query("UPDATE positions SET sellPrice = :sellPrice, positionPnl = :pnl, selltimestamp = :sellTimestamp WHERE id = :positionId")
    suspend fun closePosition(positionId: Int, sellPrice: Double?, pnl: Double?, sellTimestamp: Long = System.currentTimeMillis())
}
