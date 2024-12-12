package com.example.greenguard.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiConfig2 {
    companion object {
        fun getApiService2(): ApiService2 {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val requestWithApiKey = originalRequest.newBuilder()
                        .addHeader("x-api-key", "fa9636b762b64e209fe3fce907ac8b3c")
                        .build()
                    chain.proceed(requestWithApiKey)
                }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService2::class.java)
        }
    }
}