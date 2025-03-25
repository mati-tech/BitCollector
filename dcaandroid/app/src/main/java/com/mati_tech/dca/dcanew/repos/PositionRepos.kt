package com.mati_tech.dca.dcanew.repos

import android.content.Context
import com.mati_tech.dca.dcanew.db.AppDatabase
import com.mati_tech.dca.dcanew.db.PositionEntity

class PositionRepos(context: Context) {
    private val db = AppDatabase.getDatabase(context)

    suspend fun addPosition(position: PositionEntity) {
        db.positionDao().insertPosition(position)
    }

    suspend fun getAllPositions() = db.positionDao().getAllPositions()

    suspend fun clearAllPositions() {
        db.positionDao().deleteAllPositions()
    }
    suspend fun closePosition(positionId: Int, sellPrice: Double) {
        db.positionDao().closePosition(positionId, sellPrice, System.currentTimeMillis())
    }

    fun getCurrentPriceFromApi(coin: String): Double {

        return 23.3
    }

}
