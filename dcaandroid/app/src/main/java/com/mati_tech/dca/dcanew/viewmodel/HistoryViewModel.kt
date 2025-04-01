package com.mati_tech.dca.dcanew.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.repos.PositionRepos
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: PositionRepos) : ViewModel() {

    private val _historyPositions = MutableLiveData<List<PositionEntity>>()
    val historyPositions: LiveData<List<PositionEntity>> get() = _historyPositions

    private val _historyPositionsplus = MutableLiveData<List<PositionEntity>>()
    val historyPositionsplus: LiveData<List<PositionEntity>> get() = _historyPositionsplus

    private val _historyPositionsMinus = MutableLiveData<List<PositionEntity>>()
    val historyPositionsMinus: LiveData<List<PositionEntity>> get() = _historyPositionsMinus

    fun fetchHistory() {
        viewModelScope.launch {
            val historyData = repository.getAllPositionsHistory() // Or a filtered query for historical data
            Log.d("HistoryViewModel", "Fetched history: $historyData")
            _historyPositions.postValue(historyData)
        }
    }

    fun fetchHistoryPlusPNL() {
        viewModelScope.launch {
            val historyData = repository.getAllPositionsHistoryPlusPNL() // Or a filtered query for historical data
//            Log.d("HistoryViewModel", "Fetched history: $historyData")
            _historyPositionsplus.postValue(historyData)
        }
    }
    fun fetchHistoryNegativePNL() {
        viewModelScope.launch {
            val historyData = repository.getAllPositionsHistoryNegativePNL() // Or a filtered query for historical data
//            Log.d("HistoryViewModel", "Fetched history: $historyData")
            _historyPositionsMinus.postValue(historyData)
        }
    }

}
