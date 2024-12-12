package com.example.greenguard.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictResult(
    val diseaseName: String,
    val description : String,
    val impact : String,
    val treatment : String,
    val tipsTrick: String,
    val imageUrl: String,
) : Parcelable