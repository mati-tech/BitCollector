package com.mati_tech.dca.dcanew.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.remote.CryptoResponse
import com.mati_tech.dca.dcanew.repos.PositionRepos
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
class PositionViewModel(private val repository: PositionRepos) : ViewModel() {
    private var job: Job? = null

    private val _positions = MutableLiveData<List<PositionEntity>>()
    val positions: LiveData<List<PositionEntity>> get() = _positions

    private val _cryptoPrices = MutableLiveData<List<CryptoResponse>>()
    val cryptoPrices: LiveData<List<CryptoResponse>> = _cryptoPrices

    fun fetchPositions() {
        viewModelScope.launch {
            val positionsFromRepo = repository.getAllPositions()

            // Log the fetched positions to verify
            Log.d("PositionViewModel", "Fetched positions: $positionsFromRepo")
            _positions.postValue(repository.getAllPositions())
        }
    }

    fun startFetchingPrices(symbols: List<String>) {
        job?.cancel() // Cancel previous job if exists

        job = viewModelScope.launch (Dispatchers.IO){
            while (isActive) {  // Keeps running until ViewModel is cleared
                try {
                    val response = repository.getCurrentPriceFromApi(symbols)
//                    val priceMap = response.associate { it.symbol to it.price } // Convert list to map
                    _cryptoPrices.postValue(response)

                } catch (e: Exception) {
                    _cryptoPrices.postValue(emptyList())
                }
                delay(500L)  // Waits 0.5 second before the next API call
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

    fun closePosition(positionId: Int,pnl: Double,  sellPrice: Double?) {
        viewModelScope.launch {
            repository.closePosition(positionId,pnl, sellPrice)
            fetchPositions() // Refresh the list after closing the position
        }
    }
}
