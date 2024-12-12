package com.example.greenguard.ui.history

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class HistoryRepository private constructor(
    private val historyDao: HistoryDao,
) {

    fun getHistory(): LiveData<List<HistoryEntity>> = historyDao.getHistory()

    suspend fun removeHistory(historyEntity: HistoryEntity) {
        withContext(Dispatchers.IO) {
            historyDao.deleteHistory(historyEntity)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: HistoryRepository? = null

        fun getInstance(
            historyDao: HistoryDao
        ): HistoryRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HistoryRepository(historyDao)
            }.also { INSTANCE = it }
    }
}
