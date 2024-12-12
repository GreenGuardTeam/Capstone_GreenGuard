package com.example.greenguard.data

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("prediksi")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): FileUploadResponse
}