package com.mati_tech.dca.dcanew.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mati_tech.dca.dcanew.remoteApi.repos.old.PositionDetailRepos

class PositionDetailViewModelFactory(
    private val repository: PositionDetailRepos
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PositionDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PositionDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
