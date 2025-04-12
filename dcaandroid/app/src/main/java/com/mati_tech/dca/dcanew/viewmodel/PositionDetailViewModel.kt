//package com.mati_tech.dca.dcanew.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.mati_tech.dca.dcanew.db.PositionDetailEntity
//import com.mati_tech.dca.dcanew.repos.PositionDetailRepos
//import kotlinx.coroutines.launch
//
//class PositionDetailViewModel(private val repository: PositionDetailRepos) : ViewModel() {
//
//    private val _errorLiveData = MutableLiveData<String>()
//
//    // Public LiveData to observe error messages in the UI (Activity or Fragment)
//    val errorLiveData: LiveData<String> get() = _errorLiveData
//
//    fun insertTrade(trade: PositionDetailEntity) {
//        viewModelScope.launch {
//            repository.insertTrade(trade)
//        }
//    }
//    fun closePosition(positionId: Int, currentPrice: Double) {
//        viewModelScope.launch {
//            repository.closePosition(positionId, currentPrice)
//        }
//    }
//    fun getTradesByPositionId(positionId: Int): LiveData<List<PositionDetailEntity>> {
//        return repository.getTradesByPositionId(positionId)
//    }
//
//    fun getTotalSellValue(positionId: Int, callback: (Double) -> Unit) {
//        viewModelScope.launch {
//            val result = repository.getTotalSellValue(positionId) ?: 0.0
//            callback(result)
//        }
//    }
//
//    fun getTotalBoughtAmount(positionId: Int, callback: (Double?) -> Unit) {
//        viewModelScope.launch {
//            callback(repository.getTotalBoughtAmount(positionId))
//        }
//    }
//
//    fun getTotalCost(positionId: Int, callback: (Double?) -> Unit) {
//        viewModelScope.launch {
//            callback(repository.getTotalCost(positionId))
//        }
//    }
//
//    fun getAverageBuyPrice(positionId: Int, callback: (Double?) -> Unit) {
//        viewModelScope.launch {
//            callback(repository.getAverageBuyPrice(positionId))
//        }
//    }
//
//    fun getCurrentHeldAmount(positionId: Int, callback: (Double) -> Unit) {
//        viewModelScope.launch {
//            callback(repository.getCurrentHeldAmount(positionId))
//        }
//    }
//
//    fun getRealizedPnl(positionId: Int, callback: (Double) -> Unit) {
//        viewModelScope.launch {
//            callback(repository.getRealizedPnl(positionId))
//        }
//    }
////    fun buyAllAvailable(positionId: Int) {
////        viewModelScope.launch {
////            val lastTrade = repository.getLastTrade(positionId)
////            if (lastTrade == null) {
////                _errorLiveData.postValue("No trade history to get price from.")
////                return@launch
////            }
////
////            val price = lastTrade.tradePrice
////
////            // Get available USD
////            val availableUsd = repository.getTotalUsd(positionId) ?: 0.0
////            if (availableUsd <= 0.0) {
////                _errorLiveData.postValue("No USD available")
////                return@launch
////            }
////
////            val amountToBuy = availableUsd / price
////            buy(positionId, amountToBuy, price)
////        }
////    }
//
//
////    fun sellAllAvailable(positionId: Int) {
////        viewModelScope.launch {
////            val lastTrade = repository.getLastTrade(positionId)
////            if (lastTrade == null) {
////                _errorLiveData.postValue("No trade history to get price from.")
////                return@launch
////            }
////
////            val price = lastTrade.tradePrice
////
////            // Get available coin amount
////            val availableCoin = repository.getTotalCoin(positionId) ?: 0.0
////            if (availableCoin <= 0.0) {
////                _errorLiveData.postValue("No coin available")
////                return@launch
////            }
////
////            sell(positionId, availableCoin, price)
////        }
////    }
//
//
//    fun getUnrealizedPnl(positionId: Int, currentPrice: Double, callback: (Double?) -> Unit) {
//        viewModelScope.launch {
//            callback(repository.getUnrealizedPnl(positionId, currentPrice))
//        }
//    }
//
//    fun buy(positionId: Int, amount: Double, price: Double) {
//        viewModelScope.launch {
//            val totalCost = amount * price
//
//            val buyTrade = PositionDetailEntity(
//                positionId = positionId,
//                tradeType = "BUY",
//                tradeAmount = amount,
//                tradePrice = price,
//                totalCost = totalCost
//            )
//            repository.insertTrade(buyTrade)
//        }
//    }
//
//    fun sell(positionId: Int, amount: Double, price: Double) {
//        viewModelScope.launch {
//            val totalValue = amount * price
//            val sellTrade = PositionDetailEntity(
//                positionId = positionId,
//                tradeType = "SELL",
//                tradeAmount = amount,
//                tradePrice = price,
//                totalCost = totalValue
//            )
//            repository.insertTrade(sellTrade)
//        }
//    }
//
//}

//
//package com.mati_tech.dca.dcanew.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.mati_tech.dca.dcanew.db.PositionDetailEntity
//import com.mati_tech.dca.dcanew.repos.PositionDetailRepos
//import kotlinx.coroutines.launch
//
//class PositionDetailViewModel(private val repository: PositionDetailRepos) : ViewModel() {
//
//    private val _errorLiveData = MutableLiveData<String>()
//    val errorLiveData: LiveData<String> get() = _errorLiveData
//
//    private val _netUsdBalance = MutableLiveData<Double>()
//    val netUsdBalance: LiveData<Double> get() = _netUsdBalance
//
//    private val _currentHeldAmount = MutableLiveData<Double>()
//    val currentHeldAmount: LiveData<Double> get() = _currentHeldAmount
//
//    private val _averageBuyPrice = MutableLiveData<Double?>()
//    val averageBuyPrice: LiveData<Double?> get() = _averageBuyPrice
//
//    fun insertTrade(trade: PositionDetailEntity) {
//        viewModelScope.launch {
//            repository.insertTrade(trade)
//            updateBalances(trade.positionId)
//        }
//    }
//
//    fun getTradesByPositionId(positionId: Int): LiveData<List<PositionDetailEntity>> {
//        return repository.getTradesByPositionId(positionId)
//    }
//
//    fun buyAllAvailable(positionId: Int, currentPrice: Double) {
//        viewModelScope.launch {
//            val availableUsd = repository.getNetUsdBalance(positionId)
//            if (availableUsd <= 0.0) {
//                _errorLiveData.postValue("No USD available to buy")
//                return@launch
//            }
//            if (currentPrice <= 0.0) {
//                _errorLiveData.postValue("Invalid price")
//                return@launch
//            }
//            val amountToBuy = (availableUsd / currentPrice * 100000000.0).toLong() / 100000000.0
//            val totalCost = amountToBuy * currentPrice
//            val buyTrade = PositionDetailEntity(
//                positionId = positionId,
//                tradeType = "BUY",
//                tradeAmount = amountToBuy,
//                tradePrice = currentPrice,
//                totalCost = totalCost
//            )
//            repository.insertTrade(buyTrade)
//            updateBalances(positionId)
//        }
//    }
//
//    fun sellAllAvailable(positionId: Int, currentPrice: Double) {
//        viewModelScope.launch {
//            val availableCoin = repository.getCurrentHeldAmount(positionId)
//            if (availableCoin <= 0.0) {
//                _errorLiveData.postValue("No coins available to sell")
//                return@launch
//            }
//            if (currentPrice <= 0.0) {
//                _errorLiveData.postValue("Invalid price")
//                return@launch
//            }
//            val roundedCoin = (availableCoin * 100000000.0).toLong() / 100000000.0
//            val totalValue = (availableCoin * currentPrice * 100000000.0).toLong() / 100000000.0
//            val sellTrade = PositionDetailEntity(
//                positionId = positionId,
//                tradeType = "SELL",
//                tradeAmount = roundedCoin,
//                tradePrice = currentPrice,
//                totalCost = totalValue
//            )
//            repository.insertTrade(sellTrade)
//            updateBalances(positionId)
//        }
//    }
//
//    private fun updateBalances(positionId: Int) {
//        viewModelScope.launch {
//            _netUsdBalance.postValue(repository.getNetUsdBalance(positionId))
//            _currentHeldAmount.postValue(repository.getCurrentHeldAmount(positionId))
//            _averageBuyPrice.postValue(repository.getAverageBuyPrice(positionId))
//        }
//    }
//
//    fun fetchBalances(positionId: Int) {
//        updateBalances(positionId)
//    }
//}

package com.mati_tech.dca.dcanew.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mati_tech.dca.dcanew.db.PositionDetailEntity
import com.mati_tech.dca.dcanew.repos.PositionDetailRepos
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

class PositionDetailViewModel(private val repository: PositionDetailRepos) : ViewModel() {

    private var _availableUsd = 0.0

    private val _netUsdBalance = MutableLiveData<Double>(_availableUsd)
    val netUsdBalance: LiveData<Double> get() = _netUsdBalance

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

//    private val _netUsdBalance = MutableLiveData<Double>()
//    val netUsdBalance: LiveData<Double> get() = _netUsdBalance

    private val _currentHeldAmount = MutableLiveData<Double>()
    val currentHeldAmount: LiveData<Double> get() = _currentHeldAmount

    private val _averageBuyPrice = MutableLiveData<Double?>()
    val averageBuyPrice: LiveData<Double?> get() = _averageBuyPrice

    private val _totalPNL = MutableLiveData<Double?>()
    val totalPNL: LiveData<Double?> get() = _totalPNL

    fun insertTrade(trade: PositionDetailEntity) {
        viewModelScope.launch {
            repository.insertTrade(trade)
            updateBalances(trade.positionId)
        }
    }

    fun getTradesByPositionId(positionId: Int): LiveData<List<PositionDetailEntity>> {
        return repository.getTradesByPositionId(positionId)
    }

//    fun buyAllAvailable(positionId: Int, currentPrice: Double) {
//        viewModelScope.launch {
//            val availableUsd = repository.getLastSellUsd(positionId)
//            if (availableUsd <= 0.0) {
//                _errorLiveData.postValue("No USD available to buy")
//                return@launch
//            }
//            if (currentPrice <= 0.0) {
//                _errorLiveData.postValue("Invalid price")
//                return@launch
//            }
//            // Round to 8 decimals
//            val amountToBuy = (availableUsd / currentPrice * 100000000.0).toLong() / 100000000.0
//            val totalCost = amountToBuy * currentPrice
//            val buyTrade = PositionDetailEntity(
//                positionId = positionId,
//                tradeType = "BUY",
//                tradeAmount = amountToBuy,
//                tradePrice = currentPrice,
//                totalCost = totalCost
//            )
//            repository.insertTrade(buyTrade)
//            // Reset USD to 0 after buying
//            _netUsdBalance.postValue(0.0)
//            updateBalances(positionId)
//        }
//    }
//
//    fun sellAllAvailable(positionId: Int, currentPrice: Double) {
//        viewModelScope.launch {
//            val availableCoin = repository.getCurrentHeldAmount(positionId)
//            if (availableCoin <= 0.0) {
//                _errorLiveData.postValue("No coins available to sell")
//                return@launch
//            }
//            if (currentPrice <= 0.0) {
//                _errorLiveData.postValue("Invalid price")
//                return@launch
//            }
//            // Round to 8 decimals
//            val roundedCoin = (availableCoin * 100000000.0).toLong() / 100000000.0
//            val totalValue = roundedCoin * currentPrice
//            val sellTrade = PositionDetailEntity(
//                positionId = positionId,
//                tradeType = "SELL",
//                tradeAmount = roundedCoin,
//                tradePrice = currentPrice,
//                totalCost = totalValue
//            )
//            repository.insertTrade(sellTrade)
//            updateBalances(positionId)
//        }
//    }

    fun deleteTrade(trade: PositionDetailEntity) {
        viewModelScope.launch {
            repository.deleteTrade(trade.id)
            // If deleting a SELL trade, reset USD to 0 (since USD is in-memory)
            if (trade.tradeType == "SELL") {
                _availableUsd = 0.0
                _netUsdBalance.postValue(_availableUsd)
            }
            updateBalances(trade.positionId)
        }
    }

    fun buyAllAvailable(positionId: Int, currentPrice: Double) {
        viewModelScope.launch {
            Log.d("PositionDetailViewModel", "buyAllAvailable: availableUsd=$_availableUsd")
            if (_availableUsd <= 0.0) {
                _errorLiveData.postValue("No USD available to buy")
                return@launch
            }
            if (currentPrice <= 0.0) {
                _errorLiveData.postValue("Invalid price")
                return@launch
            }


            // Round to 8 decimals
            val amountToBuy = (_availableUsd / currentPrice * 100000000.0).toLong() / 100000000.0

            // Round tradePrice and totalCost to 2 decimals
            val roundedPrice = BigDecimal(currentPrice).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            val totalCost = BigDecimal(amountToBuy * currentPrice).setScale(2, RoundingMode.HALF_EVEN).toDouble()
//            val totalCost = amountToBuy * currentPrice

            val buyTrade = PositionDetailEntity(
                positionId = positionId,
                tradeType = "BUY",
                tradeAmount = amountToBuy,
                tradePrice = roundedPrice ,
                totalCost = totalCost
            )
            repository.insertTrade(buyTrade)
            // Reset USD to 0
            _availableUsd = 0.0
            _netUsdBalance.postValue(_availableUsd)
            updateBalances(positionId)
        }
    }

    fun sellAllAvailable(positionId: Int, currentPrice: Double) {
        viewModelScope.launch {
            val availableCoin = repository.getCurrentHeldAmount(positionId)
            Log.d("PositionDetailViewModel", "sellAllAvailable: availableCoin=$availableCoin, currentPrice=$currentPrice")
            if (availableCoin <= 0.0) {
                _errorLiveData.postValue("No coins available to sell")
                return@launch
            }
            if (currentPrice <= 0.0) {
                _errorLiveData.postValue("Invalid price")
                return@launch
            }
            // Round to 8 decimals
//            val roundedCoin = (availableCoin * 100000000.0).toLong() / 100000000.0'

            val roundedCoin = BigDecimal(availableCoin)
                .setScale(8, RoundingMode.HALF_EVEN)
                .toDouble()

            val totalValue = ((availableCoin * currentPrice) * 100).roundToInt() / 100.0

//            val totalValue = availableCoin * currentPrice

            val sellTrade = PositionDetailEntity(
                positionId = positionId,
                tradeType = "SELL",
                tradeAmount = roundedCoin,
                tradePrice = currentPrice,
                totalCost = totalValue
            )
            repository.insertTrade(sellTrade)
            // Update USD to sell proceeds
            _availableUsd = totalValue
            _netUsdBalance.postValue(_availableUsd)
            updateBalances(positionId)
        }
    }
    private fun updateBalances(positionId: Int) {
        viewModelScope.launch {

            _currentHeldAmount.postValue(repository.getCurrentHeldAmount(positionId))
            _averageBuyPrice.postValue(repository.getAverageBuyPrice(positionId))
            _totalPNL.postValue(repository.getRealizedPnl(positionId))
        }
    }

    fun fetchBalances(positionId: Int) {
        updateBalances(positionId)
    }
}