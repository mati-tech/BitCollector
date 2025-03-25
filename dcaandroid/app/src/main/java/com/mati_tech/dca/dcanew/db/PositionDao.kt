package com.mati_tech.dca.dcanew.db

import androidx.room.*

@Dao
interface PositionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosition(position: PositionEntity)

    @Query("SELECT * FROM positions ORDER BY buytimestamp DESC")
    suspend fun getAllPositions(): List<PositionEntity>

    @Query("DELETE FROM positions")
    suspend fun deleteAllPositions()
    // New method to close a position by updating sellPrice and selltimestamp

    @Query("UPDATE positions SET sellPrice = :sellPrice, selltimestamp = :sellTimestamp WHERE id = :positionId")
    suspend fun closePosition(positionId: Int, sellPrice: Double, sellTimestamp: Long = System.currentTimeMillis())
}
