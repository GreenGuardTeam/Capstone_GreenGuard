package com.example.greenguard.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class HistoryViewModel(private val historyRepository: HistoryRepository): ViewModel() {
    val historyPredictions: LiveData<List<HistoryEntity>> = historyRepository.getHistory()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadHistory(){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                historyRepository.getHistory()
                _isLoading.value = false
            } catch (exception: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun deleteHistory(historyItem: HistoryEntity) {
        viewModelScope.launch {
            historyRepository.removeHistory(historyItem)
        }
    }
}