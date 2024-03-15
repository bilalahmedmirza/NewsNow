package com.bilalmirza.newsnow.api

import com.bilalmirza.newsnow.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("api/v4/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        country: String = "pk",
        @Query("apikey")
        apiKey: String = "871a300331387910b03358f607d1d789"
    ): Response<NewsResponse>

    @GET("api/v4/search")
    suspend fun getEverything(
        @Query("q")
        q: String,
        @Query("apikey")
        apiKey: String = "871a300331387910b03358f607d1d789"
    ): Response<NewsResponse>
}