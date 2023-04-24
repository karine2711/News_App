package com.example.myapplication.rest

import com.example.myapplication.models.Article
import com.example.myapplication.models.ArticleResponse
import com.example.myapplication.models.SourceResponse
import com.example.myapplication.rest.ApiConstants.DEFAULT_COUNTRY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class NewsRepo(val apiService: NewsApiService) {

    suspend fun fetchNews(
        country: String = DEFAULT_COUNTRY,
        category: String? = null,
        query: String? = null
    ): List<Article> {
        withContext(Dispatchers.IO) {
            // simulate long request to demonstrate loading bar
            sleep(5000)
        }
        val response = apiService.fetchNews(country, category, query)
        return mapToArticle(response)
    }

    private fun mapToArticle(response: ArticleResponse): List<Article> {
        response.run {
            if ("ok" != this.status) {
                throw RuntimeException("Failed to retrieve news list. ${this.message}")
            }
            return this.articles.map {
                Article(
                    SourceResponse(it.source.id ?: "", it.source.name ?: ""),
                    it.title ?: "", it.author?.ifBlank { null }, it.urlToImage, it.content ?: "",
                    it.description ?: "", it.publishedAt, it.url
                )
            }
        }
    }

}

