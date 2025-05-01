//package com.mati_tech.dca.dcanew.repos
//
//import androidx.lifecycle.LiveData
//import com.mati_tech.dca.dcanew.dao.PositionDetailDao
//import com.mati_tech.dca.dcanew.dao.PositionDetailEntity
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
//import com.mati_tech.dca.dcanew.dao.PositionDetailDao
//import com.mati_tech.dca.dcanew.dao.PositionDetailEntity
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
package com.mati_tech.dca.dcanew.remoteApi.repos

import androidx.lifecycle.LiveData
import com.mati_tech.dca.dcanew.dao.PositionDetailDao
import com.mati_tech.dca.dcanew.dao.PositionDetailEntity

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

    suspend fun getCurrentHeldAmount(positionId: Int): Double {
        return dao.getCurrentHeldAmount(positionId)
    }

    suspend fun getAvailableUsd(positionId: Int): Double {
        return dao.getAvailableUsd(positionId) ?: 0.0
    }
    suspend fun updateAvailableUsd(positionId: Int, usd: Double) {
        dao.updateAvailableUsd(positionId, usd)
    }

    suspend fun getAverageBuyPrice(positionId: Int): Double? {
        val totalBoughtAmount = dao.getTotalBoughtAmount(positionId) ?: 0.0
        val totalBuyCost = dao.getTotalBuyCost(positionId) ?: 0.0
        return if (totalBoughtAmount > 0.0) totalBuyCost / totalBoughtAmount else null
    }

    suspend fun getRealizedPnL(positionId: Int): Double {
        val totalSellUsd = dao.getTotalSellValue(positionId) ?: 0.0
        val totalSellCoins = dao.getTotalSellCoins(positionId) ?: 0.0 // you may add this DAO method
        val avgBuyPrice = getAverageBuyPrice(positionId) ?: 0.0
        return totalSellUsd - (avgBuyPrice * totalSellCoins)
    }

    suspend fun getCurrentHeldCoins(positionId: Int): Double {
        val totalBoughtCoins = dao.getTotalBoughtAmount(positionId) ?: 0.0
        val totalSellCoins = dao.getTotalSellCoins(positionId) ?: 0.0
        return totalBoughtCoins - totalSellCoins
    }

    suspend fun getUnrealizedPnL(positionId: Int, currentMarketPrice: Double): Double {
        val avgBuyPrice = getAverageBuyPrice(positionId) ?: 0.0
        val heldCoins = getCurrentHeldCoins(positionId)
        return (currentMarketPrice - avgBuyPrice) * heldCoins
    }

    suspend fun getBreakEvenPrice(positionId: Int): Double? {
        val realizedPnl = getRealizedPnL(positionId)
        val currentHeldAmount = getCurrentHeldCoins(positionId)
        val avgBuyPrice = getAverageBuyPrice(positionId) ?: return null
        return if (currentHeldAmount > 0) {
            avgBuyPrice + (-realizedPnl / currentHeldAmount)
        } else {
            null
        }
    }
    suspend fun getTotalPnL(positionId: Int, currentMarketPrice: Double): Double {
        val realized = getRealizedPnL(positionId)
        val unrealized = getUnrealizedPnL(positionId, currentMarketPrice)
        return realized + unrealized
    }

}