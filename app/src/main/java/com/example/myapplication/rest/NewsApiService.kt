package com.example.myapplication.rest


import com.example.myapplication.models.ArticleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("/${ApiConstants.VERSION}/top-headlines/")
    suspend fun fetchNews(
        @Query("country") country: String,
        @Query("category") category: String? = null,
        @Query("q") query: String? = null,
        @Query("apiKey") apiKey: String = ApiConstants.API_KEY
    ): ArticleResponse

}