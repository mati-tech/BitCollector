//package com.mati_tech.dca.dcanew.repos
//
//import androidx.lifecycle.LiveData
//import com.mati_tech.dca.dcanew.db.PositionDetailDao
//import com.mati_tech.dca.dcanew.db.PositionDetailEntity
//
//class PositionDetailRepos(private val dao: PositionDetailDao) {
//
//    suspend fun insertTrade(trade: PositionDetailEntity) {
//        dao.insertTrade(trade)
//    }
//
//    fun getTradesByPositionId(positionId: Int): LiveData<List<PositionDetailEntity>> {
//        return dao.getTradesByPositionId(positionId)
//    }
//
//    // Method to close a position
//    suspend fun closePosition(positionId: Int, currentPrice: Double) {
//// edit this one
//        val currentHeldAmount = dao.getCurrentHeldAmount(positionId)
//            // Only close the position if there is any remaining held amount
//        if (currentHeldAmount > 0) {
//            val totalCost = currentHeldAmount * currentPrice
//            val trade = PositionDetailEntity(
//                positionId = positionId,
//                tradeType = "SELL",
//                tradeAmount = currentHeldAmount,
//                tradePrice = currentPrice,
//                totalCost = totalCost
//            )
//            dao.insertTrade(trade)  // Insert the closing trade into the database
//        }
//    }
//
//    suspend fun getTotalUsd(positionId: Int): Double {
//        val trades = dao.getAllTradesForPosition(positionId)
//        return trades
//            .filter { it.tradeType == "SELL" }
//            .sumOf { it.totalCost } -
//                trades
//                    .filter { it.tradeType == "BUY" }
//                    .sumOf { it.totalCost }
//    }
//
//    suspend fun getTotalCoin(positionId: Int): Double {
//        val trades = dao.getAllTradesForPosition(positionId)
//        return trades
//            .filter { it.tradeType == "BUY" }
//            .sumOf { it.tradeAmount } -
//                trades
//                    .filter { it.tradeType == "SELL" }
//                    .sumOf { it.tradeAmount }
//    }
//
//    suspend fun getTotalSellValue(positionId: Int): Double? {
//        return dao.getTotalSellValue(positionId)
//    }
//
//    suspend fun getTotalBoughtAmount(positionId: Int): Double? {
//        return dao.getTotalBoughtAmount(positionId)
//    }
//
//    suspend fun getTotalCost(positionId: Int): Double? {
//        return dao.getTotalBuyCost(positionId)
//    }
//    suspend fun getNetUsdBalance(positionId: Int): Double {
//        return dao.getNetUsdBalance(positionId)
//    }
//    suspend fun getAverageBuyPrice(positionId: Int): Double? {
//        return dao.getAverageBuyPrice(positionId)
//    }
//
//    suspend fun getCurrentHeldAmount(positionId: Int): Double {
//        return dao.getCurrentHeldAmount(positionId)
//    }
//
//    suspend fun getRealizedPnl(positionId: Int): Double {
//        return dao.getRealizedPnl(positionId)
//    }
//
//    suspend fun getUnrealizedPnl(positionId: Int, currentPrice: Double): Double? {
//        return dao.getUnrealizedPnl(positionId, currentPrice)
//    }
//
//    suspend fun getLastTrade(positionId: Int): PositionDetailEntity? {
//        return dao.getLastTrade(positionId)
//    }
//
//}

//
//package com.mati_tech.dca.dcanew.repos
//
//import androidx.lifecycle.LiveData
//import com.mati_tech.dca.dcanew.db.PositionDetailDao
//import com.mati_tech.dca.dcanew.db.PositionDetailEntity
//
//class PositionDetailRepos(private val dao: PositionDetailDao) {
//
//    suspend fun insertTrade(trade: PositionDetailEntity) {
//        dao.insertTrade(trade)
//    }
//
//    fun getTradesByPositionId(positionId: Int): LiveData<List<PositionDetailEntity>> {
//        return dao.getTradesByPositionId(positionId)
//    }
//
//    suspend fun getNetUsdBalance(positionId: Int): Double {
//        return dao.getNetUsdBalance(positionId)
//    }
//
//    suspend fun getTotalSellValue(positionId: Int): Double? {
//        return dao.getTotalSellValue(positionId)
//    }
//
//    suspend fun getTotalBoughtAmount(positionId: Int): Double? {
//        return dao.getTotalBoughtAmount(positionId)
//    }
//
//    suspend fun getTotalBuyCost(positionId: Int): Double? {
//        return dao.getTotalBuyCost(positionId)
//    }
//
//    suspend fun getAverageBuyPrice(positionId: Int): Double? {
//        return dao.getAverageBuyPrice(positionId)
//    }
//
//    suspend fun getCurrentHeldAmount(positionId: Int): Double {
//        return dao.getCurrentHeldAmount(positionId)
//    }
//
//    suspend fun getRealizedPnl(positionId: Int): Double {
//        return dao.getRealizedPnl(positionId)
//    }
//
//    suspend fun getUnrealizedPnl(positionId: Int, currentPrice: Double): Double? {
//        return dao.getUnrealizedPnl(positionId, currentPrice)
//    }
//
//    suspend fun getLastTrade(positionId: Int): PositionDetailEntity? {
//        return dao.getLastTrade(positionId)
//    }
//}
package com.mati_tech.dca.dcanew.repos

import androidx.lifecycle.LiveData
import com.mati_tech.dca.dcanew.db.PositionDetailDao
import com.mati_tech.dca.dcanew.db.PositionDetailEntity

class PositionDetailRepos(private val dao: PositionDetailDao) {

    suspend fun insertTrade(trade: PositionDetailEntity) {
        dao.insertTrade(trade)
    }
    suspend fun deleteTrade(tradeId: Int) {
        dao.deleteTrade(tradeId)
    }
    fun getTradesByPositionId(positionId: Int): LiveData<List<PositionDetailEntity>> {
        return dao.getTradesByPositionId(positionId)
    }

    suspend fun getTotalSellValue(positionId: Int): Double? {
        return dao.getTotalSellValue(positionId)
    }

    suspend fun getTotalBoughtAmount(positionId: Int): Double? {
        return dao.getTotalBoughtAmount(positionId)
    }

    suspend fun getTotalBuyCost(positionId: Int): Double? {
        return dao.getTotalBuyCost(positionId)
    }

    suspend fun getAverageBuyPrice(positionId: Int): Double? {
        return dao.getAverageBuyPrice(positionId)
    }

    suspend fun getCurrentHeldAmount(positionId: Int): Double {
        return dao.getCurrentHeldAmount(positionId)
    }

    suspend fun getRealizedPnl(positionId: Int): Double {
        return dao.getRealizedPnl(positionId)
    }

    suspend fun getUnrealizedPnl(positionId: Int, currentPrice: Double): Double? {
        return dao.getUnrealizedPnl(positionId, currentPrice)
    }

    suspend fun getLastTrade(positionId: Int): PositionDetailEntity? {
        return dao.getLastTrade(positionId)
    }
}