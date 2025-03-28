package com.mati_tech.dca.dcanew.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.repos.PositionRepos
import com.mati_tech.dca.dcanew.ui.PositionAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
class PositionViewModel(private val repository: PositionRepos) : ViewModel() {
    private var job: Job? = null
    private val _positions = MutableLiveData<List<PositionEntity>>()
    val positions: LiveData<List<PositionEntity>> get() = _positions

    private val _cryptoPrice = MutableLiveData<String>()
    val cryptoPrice: LiveData<String> = _cryptoPrice


    fun fetchPositions() {
        viewModelScope.launch {
            _positions.postValue(repository.getAllPositions())
        }
    }

//    fun fetchCurrentPrice(position: PositionEntity, onPriceFetched: (Double) -> Unit) {
//        viewModelScope.launch {
//            try {
//                val price = repository.getCurrentPriceFromApi(position.coin)
//                onPriceFetched(price)  // Pass the fetched price to the adapter
//            } catch (e: Exception) {
//                Log.e("PositionViewModel", "Error fetching price", e)
//            }
//        }
//    }


    fun fetchAndUpdatePrices(adapter: PositionAdapter) {
        viewModelScope.launch {
            positions.value?.forEachIndexed { index, position ->
                try {
                    val newPrice = repository.getCurrentPriceFromApi(position.coin) // API Call
                    adapter.updateCurrentPrice(index, newPrice.price.toDouble()) // Update UI
                } catch (e: Exception) {
                    Log.e("PositionViewModel", "Error fetching price", e)
                }
            }
        }
    }

    fun startFetchingPrice(symbol: String) {
        job?.cancel() // Cancel previous job if exists

        job = viewModelScope.launch {
            while (isActive) {  // Keeps running until ViewModel is cleared
                try {
                    val response = repository.getCurrentPriceFromApi(symbol)
                    _cryptoPrice.postValue("Price: ${response.price}")
                } catch (e: Exception) {
                    _cryptoPrice.postValue("Error: ${e.message}")
                }
                delay(1000L)  // Waits 1 second before the next API call
            }
        }
    }

    fun stopFetchingPrice() {
        job?.cancel()  // Stops the loop
    }

    fun addPosition(position: PositionEntity) {
        viewModelScope.launch {
            repository.addPosition(position)
            fetchPositions()
        }
    }

    fun clearPositions() {
        viewModelScope.launch {
            repository.clearAllPositions()
            _positions.postValue(emptyList())
        }
    }

    fun closePosition(positionId: Int, sellPrice: Double?) {
        viewModelScope.launch {
            repository.closePosition(positionId, sellPrice)
            fetchPositions() // Refresh the list after closing the position
        }
    }
}
