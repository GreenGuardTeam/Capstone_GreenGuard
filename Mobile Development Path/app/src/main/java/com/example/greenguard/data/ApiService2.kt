package com.example.greenguard.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService2 {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("q") query: String = "plant",
        @Query("category") category: String = "science",
        @Query("language") language: String = "en",
    ): Response<NewsResponse>

}

