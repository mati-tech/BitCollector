package com.mati_tech.dca.dcanew.remoteApi.repos

import com.mati_tech.dca.dcanew.dao.PositionDao
import com.mati_tech.dca.dcanew.dao.PositionEntity
import com.mati_tech.dca.dcanew.remoteApi.cryptodata.CryptoResponse
import com.mati_tech.dca.dcanew.remoteApi.cryptodata.RetrofitInstance


class PositionRepos(private val positionDao: PositionDao) {

    suspend fun addPosition(position: PositionEntity) {
        positionDao.insertPosition(position)
    }

    suspend fun getAllPositions() = positionDao.getAllPositions()
    suspend fun getPositionbyId(id: Int) = positionDao.getPositionById(id)

    suspend fun getTotalinPositions() = positionDao.getTotalInPositions()

    suspend fun getAllPositionsHistory() = positionDao.getAllPositionsHistory()

    suspend fun getAllPositionsHistoryPlusPNL() = positionDao.getAllPositionsHistoryPlusPNL()

    suspend fun getAllPositionsHistoryNegativePNL() = positionDao.getAllPositionsHistoryNegativePNL()

    suspend fun clearAllPositions() = positionDao.deleteAllPositions()

    suspend fun closePosition(positionId: Int, pnl: Double?, sellPrice: Double?) {
        positionDao.closePosition(positionId, sellPrice, pnl, System.currentTimeMillis())
    }

    suspend fun deletePosition(position: PositionEntity) = positionDao.deletePosition(position)

    suspend fun updatePosition(id: Int, newPrice: Double, newAmount: Double, newTotalUsd: Double) {
        positionDao.updatePosition(id, newPrice, newAmount, newTotalUsd)
    }

    suspend fun updatehasDetails(positionId: Int, hasDetails: Boolean) {
        positionDao.updateHasDetails(positionId, hasDetails)
    }

    suspend fun getCurrentPriceFromApi(symbols: List<String>): List<CryptoResponse> {
        val allCoins = RetrofitInstance.api.getCoinPrices()
        return allCoins.filter { coin -> symbols.contains(coin.symbol) }
    }
}
