package com.mati_tech.dca.dcanew.remoteApi.repos

import com.mati_tech.dca.dcanew.dao.PositionEntity
import com.mati_tech.dca.dcanew.remoteApi.client.MexcApi
import com.mati_tech.dca.dcanew.remoteApi.cryptodata.CryptoResponse
import com.mati_tech.dca.dcanew.remoteApi.models.ApiResponse
import com.mati_tech.dca.dcanew.remoteApi.models.Position
import com.mati_tech.dca.dcanew.remoteApi.services.PositionApiService
import com.mati_tech.dca.dcanew.remoteApi.services.UserApiService
import retrofit2.Response

class PositionReposRemote (private val positionApi: PositionApiService) {

//    suspend fun addPosition(token: String, position: Position): Response<ApiResponse> {
//        return positionApi.createPosition(token, position)
//    }
//
//    suspend fun getAllPositions(token: String, userId: Int): Response<List<Position>> {
//        return positionApi.getPositionsByUserId(token, userId)
//    }
//
//    suspend fun getPositionById(token: String, positionId: Int): Response<Position> {
//        return positionApi.getPositionById(token, positionId)
//    }
//    suspend fun getTotalinPositions() = positionDao.getTotalInPositions()
//
//    suspend fun getAllPositionsHistory() = positionDao.getAllPositionsHistory()
//
//    suspend fun getAllPositionsHistoryPlusPNL() = positionDao.getAllPositionsHistoryPlusPNL()
//
//    suspend fun getAllPositionsHistoryNegativePNL() = positionDao.getAllPositionsHistoryNegativePNL()
//
//    suspend fun clearAllPositions() = positionDao.deleteAllPositions()
//
//    suspend fun closePosition(positionId: Int, pnl: Double?, sellPrice: Double?) {
//        positionDao.closePosition(positionId, sellPrice, pnl, System.currentTimeMillis())
//    }

//    suspend fun deletePosition(position: PositionEntity) = positionDao.deletePosition(position)
//
//    suspend fun updatePosition(token: String, positionId: Int, position: Position): Response<String> {
//        return positionApi.updatePosition(token, positionId, position)
//    }
//
//    suspend fun updatehasDetails(positionId: Int, hasDetails: Boolean) {
//        positionDao.updateHasDetails(positionId, hasDetails)
//    }

//    suspend fun getCurrentPriceFromApi(symbols: List<String>): List<CryptoResponse> {
//        val allCoins = MexcApi.api.getCoinPrices()
//        return allCoins.filter { coin -> symbols.contains(coin.symbol) }
//    }
}