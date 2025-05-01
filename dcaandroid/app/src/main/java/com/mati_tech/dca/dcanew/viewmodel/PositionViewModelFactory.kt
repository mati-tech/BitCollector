package com.mati_tech.dca.dcanew.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mati_tech.dca.dcanew.remoteApi.repos.PositionRepos

class PositionViewModelFactory(private val repository: PositionRepos) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PositionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PositionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
