package com.mati_tech.dca.dcanew.repos

import android.content.Context
import com.mati_tech.dca.dcanew.db.AppDatabase
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.remote.CryptoResponse
import com.mati_tech.dca.dcanew.remote.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class PositionRepos(context: Context) {
    private val db = AppDatabase.getDatabase(context)

    suspend fun addPosition(position: PositionEntity) {
        db.positionDao().insertPosition(position)
    }

    suspend fun getAllPositions() = db.positionDao().getAllPositions()

    suspend fun clearAllPositions() {
        db.positionDao().deleteAllPositions()
    }
    suspend fun closePosition(positionId: Int, sellPrice: Double?) {
        db.positionDao().closePosition(positionId, sellPrice, System.currentTimeMillis())
    }

    suspend fun getCurrentPriceFromApi(symbols: List<String>): List<CryptoResponse> {
        // Fetch all coins from the API
        val allCoins = RetrofitInstance.api.getCoinPrices()

        // Filter the response to include only the requested symbols
        return allCoins.filter { coin ->
            symbols.contains(coin.symbol)
        }
    }
//        val symbolsQuery = symbols.joinToString(",")



//        return RetrofitInstance.api.getCoinPrices()


}
