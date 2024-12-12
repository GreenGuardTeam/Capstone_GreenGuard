package com.example.greenguard.ui.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val selectedImage : String,
    val plantDisease: String,
    val description: String,
    val impact: String,
    val treatment: String,
    val tipsTrick :String,
    @ColumnInfo(name = "isHistory")
    var isHistory: Boolean = false
)
