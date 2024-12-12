package com.example.greenguard.ui.history

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getHistory(): LiveData<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(prediction: HistoryEntity)

    @Delete
    suspend fun deleteHistory(currentHistory: HistoryEntity)

    @Query("DELETE FROM history")
    fun deleteAllHistories()
}