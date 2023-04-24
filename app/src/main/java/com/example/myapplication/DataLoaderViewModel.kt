package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.exception.EmptyResponseException
import com.example.myapplication.models.Article
import com.example.myapplication.rest.NewsApiService
import com.example.myapplication.rest.NewsRepo
import com.example.myapplication.rest.RetrofitHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class MainViewModel : ViewModel() {

    private val _articles = MutableLiveData<Result<List<Article>>>()
    val articles: LiveData<Result<List<Article>>> = _articles
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    private val newsRepo = NewsRepo(
        RetrofitHelper.getInstance().create(NewsApiService::class.java)
    )

    fun getArticles() {
        viewModelScope.launch {
            val prevData: List<Article>? = (_articles.value as? Result.Success<List<Article>>)?.data
            _articles.postValue(Result.loading())
            try {
                searchByApi(NewsOptions.category, NewsOptions.query)
            } catch (e: Exception) {
                val filtered = prevData?.let { getFiltered(it, NewsOptions.query) } ?: emptyList()
                if (filtered.isNotEmpty()) {
                    _articles.postValue(Result.success(filtered))
                } else {
                    _articles.postValue(Result.error(EmptyResponseException("No results found")))
                }
            }
        }
        _isRefreshing.tryEmit(false)
    }

    private suspend fun searchByApi(category: String?, query: String?) {
        try {
            val response = newsRepo.fetchNews(category = category, query = query)
            if (response.isEmpty()) {
                throw EmptyResponseException("No results found")
            }
            _articles.postValue(Result.success(response))
        } catch (e: EmptyResponseException) {
            e.message?.let { Log.e("Couldn't perform search", it) }
            _articles.postValue(Result.error(e))
        }
    }

    private fun getFiltered(articles: List<Article>, query: String?): List<Article> {
        if (query == null) return articles
        return articles.filter { a -> containsKeyWord(a, query) }
    }

    private fun containsKeyWord(
        a: Article,
        query: String
    ) = a.author?.contains(query, true) ?: false ||
            a.source.name.contains(query) ||
            a.content.contains(query) ||
            a.url.contains(query) ||
            a.description.contains(query) ||
            a.title.contains(query)

}

sealed class Result<out T : Any> {
    data class Loading(val message: String = "") : Result<Nothing>()
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    companion object {
        fun <T : Any> loading(message: String = ""): Result<T> = Loading(message)
        fun <T : Any> success(data: T): Result<T> = Success(data)
        fun error(exception: Exception): Result<Nothing> = Error(exception)
    }
}