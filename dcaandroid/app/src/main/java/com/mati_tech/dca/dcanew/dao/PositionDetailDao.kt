//package com.mati_tech.dca.dcanew.db
//
//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//
//@Dao
//interface PositionDetailDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertTrade(position: PositionDetailEntity)
//
//    @Query("SELECT * FROM position_details WHERE positionId = :positionId ORDER BY id ASC")
//    fun getTradesByPositionId(positionId: Int): LiveData<List<PositionDetailEntity>>
//
//    @Query("SELECT SUM(totalCost) FROM position_details WHERE positionId = :positionId AND tradeType = 'SELL'")
//    suspend fun getTotalSellValue(positionId: Int): Double?
//
//
//    @Query("SELECT SUM(tradeAmount) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'")
//    suspend fun getTotalBoughtAmount(positionId: Int): Double?
//
//    @Query("SELECT SUM(totalCost) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'")
//    suspend fun getTotalCost(positionId: Int): Double?
//
//    suspend fun getAverageBuyPrice(positionId: Int): Double? {
//        val totalBoughtAmount = getTotalBoughtAmount(positionId) ?: 0.0
//        val totalCost = getTotalCost(positionId) ?: 0.0
//        return if (totalBoughtAmount > 0.0) totalCost / totalBoughtAmount else 0.0
//    }
//
//    @Query("""
//        SELECT COALESCE(
//            (SELECT SUM(tradeAmount) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'), 0
//        ) -
//        COALESCE(
//            (SELECT SUM(tradeAmount) FROM position_details WHERE positionId = :positionId AND tradeType = 'SELL'), 0
//        )
//    """)
//    suspend fun getCurrentHeldAmount(positionId: Int): Double
//
//    suspend fun getRealizedPnl(positionId: Int): Double {
////        val totalSellValue = getTotalSellValue(positionId)
////        val totalBuyCost = getTotalCost(positionId)
////        return totalSellValue - totalBuyCost
//        return 0.0
//    }
//
//    suspend fun getUnrealizedPnl(positionId: Int, currentPrice: Double): Double? {
//        val currentHeldAmount = getCurrentHeldAmount(positionId)
//        val avgBuyPrice = getAverageBuyPrice(positionId)
//        return (currentPrice - avgBuyPrice!!) * currentHeldAmount
//    }
//    suspend fun closePosition(positionId: Int, currentPrice: Double) {
//        val currentHeldAmount = getCurrentHeldAmount(positionId)
//
//        // Only close the position if there is any remaining held amount
//        if (currentHeldAmount > 0) {
//            val totalCost = currentHeldAmount * currentPrice
//            val trade = PositionDetailEntity(
//                positionId = positionId,
//                tradeType = "SELL",
//                tradeAmount = currentHeldAmount,
//                tradePrice = currentPrice,
//                totalCost = totalCost
//            )
//            insertTrade(trade)  // Insert the closing trade into the database
//        }
//    }
//
//    @Query("SELECT * FROM position_details WHERE positionId = :positionId ORDER BY timestamp DESC LIMIT 1")
//    suspend fun getLastTrade(positionId: Int): PositionDetailEntity?
//
//    @Query("SELECT * FROM position_details WHERE positionId = :positionId")
//    suspend fun getAllTradesForPosition(positionId: Int): List<PositionDetailEntity>
//
//
//}

//package com.mati_tech.dca.dcanew.db
//
//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//
//@Dao
//interface PositionDetailDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertTrade(position: PositionDetailEntity)
//
//    @Query("SELECT * FROM position_details WHERE positionId = :positionId ORDER BY timestamp ASC")
//    fun getTradesByPositionId(positionId: Int): LiveData<List<PositionDetailEntity>>
//
//    @Query("SELECT SUM(totalCost) FROM position_details WHERE positionId = :positionId AND tradeType = 'SELL'")
//    suspend fun getTotalSellValue(positionId: Int): Double?
//
//    @Query("SELECT SUM(tradeAmount) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'")
//    suspend fun getTotalBoughtAmount(positionId: Int): Double?
//
//    @Query("SELECT SUM(totalCost) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'")
//    suspend fun getTotalBuyCost(positionId: Int): Double?
//
////    @Query("""
////        SELECT COALESCE(
////            (SELECT SUM(totalCost) FROM position_details WHERE positionId = :positionId AND tradeType = 'SELL'), 0
////        ) -
////        COALESCE(
////            (SELECT SUM(totalCost) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'), 0
////        )
////    """)
////    suspend fun getNetUsdBalance(positionId: Int): Double
//
//    @Query("""
//        SELECT CASE
//            WHEN (SELECT COUNT(*) FROM position_details WHERE positionId = :positionId) = 0 THEN 0.0
//            WHEN (SELECT COUNT(*) FROM position_details WHERE positionId = :positionId AND tradeType = 'SELL') = 0 THEN 0.0
//            ELSE COALESCE(
//                (SELECT SUM(totalCost) FROM position_details WHERE positionId = :positionId AND tradeType = 'SELL'), 0
//            ) -
//            COALESCE(
//                (SELECT SUM(totalCost) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'), 0
//            )
//        END
//    """)
//    suspend fun getNetUsdBalance(positionId: Int): Double
//
//    suspend fun getAverageBuyPrice(positionId: Int): Double? {
//        val totalBoughtAmount = getTotalBoughtAmount(positionId) ?: 0.0
//        val totalCost = getTotalBuyCost(positionId) ?: 0.0
//        return if (totalBoughtAmount > 0.0) totalCost / totalBoughtAmount else null
//    }
//
//    @Query("""
//        SELECT COALESCE(
//            (SELECT SUM(tradeAmount) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'), 0
//        ) -
//        COALESCE(
//            (SELECT SUM(tradeAmount) FROM position_details WHERE positionId = :positionId AND tradeType = 'SELL'), 0
//        )
//    """)
//    suspend fun getCurrentHeldAmount(positionId: Int): Double
//
//    suspend fun getRealizedPnl(positionId: Int): Double {
//        val totalSellValue = getTotalSellValue(positionId) ?: 0.0
//        val totalBuyCost = getTotalBuyCost(positionId) ?: 0.0
//        return totalSellValue - totalBuyCost
//    }
//
//    suspend fun getUnrealizedPnl(positionId: Int, currentPrice: Double): Double? {
//        val currentHeldAmount = getCurrentHeldAmount(positionId)
//        val avgBuyPrice = getAverageBuyPrice(positionId) ?: return null
//        return (currentPrice - avgBuyPrice) * currentHeldAmount
//    }
//
//    @Query("SELECT * FROM position_details WHERE positionId = :positionId ORDER BY timestamp DESC LIMIT 1")
//    suspend fun getLastTrade(positionId: Int): PositionDetailEntity?
//
//    @Query("SELECT * FROM position_details WHERE positionId = :positionId")
//    suspend fun getAllTradesForPosition(positionId: Int): List<PositionDetailEntity>
//}


package com.mati_tech.dca.dcanew.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PositionDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(position: PositionDetailEntity)

    @Query("SELECT * FROM position_details WHERE positionId = :positionId ORDER BY timestamp ASC")
    fun getTradesByPositionId(positionId: Int): LiveData<List<PositionDetailEntity>>

    @Query("SELECT COALESCE(SUM(tradeAmount), 0) FROM position_details WHERE positionId = :positionId AND tradeType = 'SELL'")
    suspend fun getTotalSellCoins(positionId: Int): Double?

    @Query("SELECT SUM(totalCost) FROM position_details WHERE positionId = :positionId AND tradeType = 'SELL'")
    suspend fun getTotalSellValue(positionId: Int): Double?

    @Query("SELECT SUM(tradeAmount) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'")
    suspend fun getTotalBoughtAmount(positionId: Int): Double?

    @Query("SELECT SUM(totalCost) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'")
    suspend fun getTotalBuyCost(positionId: Int): Double?

    @Query("SELECT availableUsd FROM position_details WHERE positionId = :positionId")
    suspend fun getAvailableUsd(positionId: Int): Double?

    @Query("UPDATE position_details SET availableUsd = :usd WHERE positionId = :positionId")
    suspend fun updateAvailableUsd(positionId: Int, usd: Double)



//    @Query("""
//        SELECT COALESCE(
//            (SELECT totalCost FROM position_details
//             WHERE positionId = :positionId AND tradeType = 'SELL'
//             ORDER BY timestamp DESC LIMIT 1),
//            0.0
//        )
//    """)
//    suspend fun getLastSellUsd(positionId: Int): Double

    suspend fun getAverageBuyPrice(positionId: Int): Double? {
        val totalBoughtAmount = getTotalBoughtAmount(positionId) ?: 0.0
        val totalCost = getTotalBuyCost(positionId) ?: 0.0
        return if (totalBoughtAmount > 0.0) totalCost / totalBoughtAmount else null
    }

    @Query("DELETE FROM position_details WHERE id = :tradeId")
    suspend fun deleteTrade(tradeId: Int)
    @Query("""
        SELECT COALESCE(
            (SELECT SUM(tradeAmount) FROM position_details WHERE positionId = :positionId AND tradeType = 'BUY'), 0
        ) - 
        COALESCE(
            (SELECT SUM(tradeAmount) FROM position_details WHERE positionId = :positionId AND tradeType = 'SELL'), 0
        )
    """)
    suspend fun getCurrentHeldAmount(positionId: Int): Double

    suspend fun getRealizedPnl(positionId: Int): Double {
        val totalSellValue = getTotalSellValue(positionId) ?: 0.0
        val totalSoldAmount = getTotalSellCoins(positionId) ?: 0.0
        val avgBuyPrice = getAverageBuyPrice(positionId) ?: 0.0
        val costOfSoldCoins = totalSoldAmount * avgBuyPrice
        return totalSellValue - costOfSoldCoins
    }

    suspend fun getUnrealizedPnl(positionId: Int, currentPrice: Double): Double? {
        val currentHeldAmount = getCurrentHeldAmount(positionId)
        val avgBuyPrice = getAverageBuyPrice(positionId) ?: return null
        return (currentPrice - avgBuyPrice) * currentHeldAmount
    }
    suspend fun getBreakEvenPrice(positionId: Int): Double? {
        val realizedPnl = getRealizedPnl(positionId)
        val currentHeldAmount = getCurrentHeldAmount(positionId)
        val avgBuyPrice = getAverageBuyPrice(positionId) ?: return null
        return if (currentHeldAmount > 0) {
            avgBuyPrice + (-realizedPnl / currentHeldAmount)
        } else {
            null
        }
    }
    @Query("SELECT * FROM position_details WHERE positionId = :positionId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastTrade(positionId: Int): PositionDetailEntity?

    @Query("SELECT * FROM position_details WHERE positionId = :positionId")
    suspend fun getAllTradesForPosition(positionId: Int): List<PositionDetailEntity>
}