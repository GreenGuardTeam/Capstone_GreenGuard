package com.example.greenguard.ui.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greenguard.data.ApiConfig2
import com.example.greenguard.data.ApiService2
import com.example.greenguard.ui.home.DetailNewsViewModel
import com.example.greenguard.ui.home.HomeViewModel

class ViewModelFactory private constructor(
    private val historyRepository: HistoryRepository,
    private val apiService2: ApiService2
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(historyRepository) as T
            }
            modelClass.isAssignableFrom(DetailNewsViewModel::class.java) -> {
                DetailNewsViewModel(apiService2) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(apiService2) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    HistoryRepository.getInstance(
                        HistoryDatabase.getDatabase(context).historyDao()
                    ),
                    ApiConfig2.getApiService2()
                ).also { INSTANCE = it }
            }
        }
    }
}