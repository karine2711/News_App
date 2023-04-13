package com.example.myapplication.rest

import com.example.myapplication.models.Article
import com.example.myapplication.models.SourceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class NewsRepo(val apiService: NewsApiService) {
    suspend fun fetchNews(country: String): List<Article> {
        withContext(Dispatchers.IO) {
            // simulate long request to demonstrate loading bar
            sleep(5000)
        }
        return apiService.fetchNews(country).run {
            if ("ok" != this.status) {
                throw RuntimeException("Failed to retrieve news list. ${this.message}")
            }
            this.articles.map {
                Article(
                    SourceResponse(it.source.id ?: "", it.source.name ?: ""),
                    it.title ?: "", it.author ?: "", it.urlToImage
                )
            }
        }
    }
}

