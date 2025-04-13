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

    private val _totalPnl = MutableLiveData<Double>()
    val totalPnl: LiveData<Double> = _totalPnl

    private var currentPrice: Double = 0.0


    private val _availableUsd = MutableLiveData<Double>()
    val availableUsd: LiveData<Double> get() = _availableUsd


    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData


    private val _currentHeldAmount = MutableLiveData<Double>()
    val currentHeldAmount: LiveData<Double> get() = _currentHeldAmount

    private val _averageBuyPrice = MutableLiveData<Double?>()
    val averageBuyPrice: LiveData<Double?> get() = _averageBuyPrice

    private val _totalPNL = MutableLiveData<Double?>()
    val totalPNL: LiveData<Double?> get() = _totalPNL

    fun updatePriceAndCalculatePnl(newPrice: Double, positionId: Int) {
        currentPrice = newPrice
        viewModelScope.launch {
            val realized = repository.getRealizedPnL(positionId)
            val unrealized = repository.getUnrealizedPnL(positionId, currentPrice)
            _totalPnl.value = realized + unrealized
        }
    }
    fun insertTrade(trade: PositionDetailEntity) {
        viewModelScope.launch {
            repository.insertTrade(trade)
            updateBalances(trade.positionId)
        }
    }

    fun getTradesByPositionId(positionId: Int): LiveData<List<PositionDetailEntity>> {
        return repository.getTradesByPositionId(positionId)
    }


    fun deleteTrade(trade: PositionDetailEntity) {
        viewModelScope.launch {
            repository.deleteTrade(trade.id)
            updateBalances(trade.positionId)
        }
    }

    fun buyAllAvailable(positionId: Int, currentPrice: Double) {
        viewModelScope.launch {
            Log.d("PositionDetailViewModel", "buyAllAvailable: availableUsd=$_availableUsd")
            val availableUsd = repository.getAvailableUsd(positionId)
            if (availableUsd <= 0.0) {
                _errorLiveData.postValue("No USD available to buy")
                return@launch
            }
            if (currentPrice <= 0.0) {
                _errorLiveData.postValue("Invalid price")
                return@launch
            }

            // Round to 8 decimals
            val amountToBuy = (availableUsd / currentPrice * 100000000.0).toLong() / 100000000.0

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
            repository.updateAvailableUsd(positionId, 0.0)
            _availableUsd.postValue(0.0)
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

            val roundedCoin = BigDecimal(availableCoin)
                .setScale(8, RoundingMode.HALF_EVEN)
                .toDouble()

            val totalValue = ((availableCoin * currentPrice) * 100).roundToInt() / 100.0

            val sellTrade = PositionDetailEntity(
                positionId = positionId,
                tradeType = "SELL",
                tradeAmount = roundedCoin,
                tradePrice = currentPrice,
                totalCost = totalValue
            )
            repository.insertTrade(sellTrade)

            repository.updateAvailableUsd(positionId, totalValue)
            _availableUsd.postValue(totalValue)
            updateBalances(positionId)
        }
    }
    private fun updateBalances(positionId: Int) {
        viewModelScope.launch {
            val usd = repository.getAvailableUsd(positionId)
            _availableUsd.postValue(usd)
            _currentHeldAmount.postValue(repository.getCurrentHeldAmount(positionId))
            _averageBuyPrice.postValue(repository.getAverageBuyPrice(positionId))
            _totalPNL.postValue(repository.getTotalPnL(positionId, currentPrice))
        }
    }

    fun fetchBalances(positionId: Int) {
        updateBalances(positionId)
    }

}

