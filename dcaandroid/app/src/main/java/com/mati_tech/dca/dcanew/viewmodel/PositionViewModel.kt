package com.mati_tech.dca.dcanew.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.repos.PositionRepos
import kotlinx.coroutines.launch

class PositionViewModel(private val repository: PositionRepos) : ViewModel() {

    private val _positions = MutableLiveData<List<PositionEntity>>()
    val positions: LiveData<List<PositionEntity>> get() = _positions

    fun fetchPositions() {
        viewModelScope.launch {
            _positions.postValue(repository.getAllPositions())
        }
    }

    fun fetchCurrentPrice(position: PositionEntity, onPriceFetched: (Double) -> Unit) {
        viewModelScope.launch {
            try {
                val price = repository.getCurrentPriceFromApi(position.coin)
                onPriceFetched(price)  // Pass the fetched price to the adapter
            } catch (e: Exception) {
                Log.e("PositionViewModel", "Error fetching price", e)
            }
        }
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

    fun closePosition(positionId: Int, sellPrice: Double) {
        viewModelScope.launch {
            repository.closePosition(positionId, sellPrice)
            fetchPositions() // Refresh the list after closing the position
        }
    }
}
