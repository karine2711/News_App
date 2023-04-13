package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.Article
import com.example.myapplication.rest.NewsApiService
import com.example.myapplication.rest.NewsRepo
import com.example.myapplication.rest.RetrofitHelper
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _articles = MutableLiveData<Result<List<Article>>>()
    val articles: LiveData<Result<List<Article>>> = _articles

    fun loadNews() {
        viewModelScope.launch {
            _articles.postValue(Result.loading())
            try {
                val response = NewsRepo(
                    RetrofitHelper.getInstance().create(NewsApiService::class.java)
                ).fetchNews("us")
                _articles.postValue(Result.success(response))
            } catch (e: Exception) {
                e.message?.let { Log.e("Couldn't load view model", it) }
                _articles.postValue(Result.error(e))
            }
        }
    }
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