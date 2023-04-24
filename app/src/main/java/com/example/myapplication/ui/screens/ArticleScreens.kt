package com.example.myapplication.ui.screens

sealed class ArticleScreens(val route: String) {
    object HomeScreen : ArticleScreens("home_screen")
    object DetailScreen : ArticleScreens("detail_screen")
}