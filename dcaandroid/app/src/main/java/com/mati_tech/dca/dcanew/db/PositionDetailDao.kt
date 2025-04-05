package com.mati_tech.dca.dcanew.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PositionDetailDao {

    @Query("SELECT SUM(tradeAmount) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'")
    suspend fun getTotalBoughtAmount(positionId: Int): Double

}