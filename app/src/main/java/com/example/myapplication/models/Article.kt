package com.example.myapplication.models

data class ArticleResponse(
    val articles: List<Article>,
    val status: String,
    val message: String
)

data class Article(
    val source: SourceResponse,
    val title: String,
    val author: String?,
    val urlToImage: String?,
    val content:String,
    val description:String,
    val publishedAt: String,
    val url:String
)

data class SourceResponse(
    val id: String,
    val name: String
)
