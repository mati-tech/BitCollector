package com.mati_tech.dca.dcanew.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mati_tech.dca.dcanew.dao.PositionEntity
import com.mati_tech.dca.dcanew.remoteApi.repos.old.PositionRepos
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: PositionRepos) : ViewModel() {

    private val _historyPositions = MutableLiveData<List<PositionEntity>>()
    val historyPositions: LiveData<List<PositionEntity>> get() = _historyPositions

    private val _historyPositionsplus = MutableLiveData<Double?>()
    val historyPositionsplus: LiveData<Double?> get() = _historyPositionsplus

    private val _historyPositionsMinus = MutableLiveData<Double?>()
    val historyPositionsMinus: LiveData<Double?> get() = _historyPositionsMinus

    fun fetchHistory() {
        viewModelScope.launch {
            val historyData = repository.getAllPositionsHistory() // Or a filtered query for historical data
            Log.d("HistoryViewModel", "Fetched history: $historyData")
            _historyPositions.postValue(historyData)
        }
    }

    fun fetchHistoryPlusPNL() {
        viewModelScope.launch {
            val historyData = repository.getTotalPositivePNL() // Or a filtered query for historical data
//            Log.d("HistoryViewModel", "Fetched history: $historyData")
            _historyPositionsplus.postValue(historyData)
        }
    }
    fun fetchHistoryNegativePNL() {
        viewModelScope.launch {
            val historyData = repository.getTotalNegativePNL() // Or a filtered query for historical data
//            Log.d("HistoryViewModel", "Fetched history: $historyData")
            _historyPositionsMinus.postValue(historyData)
        }
    }

    fun deletePosition(position: PositionEntity){
        viewModelScope.launch {
            repository.deletePosition(position)
            fetchHistory()
        }
    }

    fun updatePosition(id: Int, newPrice: Double, newAmount: Double, newTotalUsd: Double) {
        viewModelScope.launch {
            repository.updatePosition(id, newPrice, newAmount, newTotalUsd)
        }
    }


}
