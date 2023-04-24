package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.ArticleListScreen
import com.example.myapplication.ui.screens.ArticleScreens
import com.example.myapplication.ui.screens.ExpandedArticleScreen


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getArticles()
        viewModel.articles.observe(this) { articleList ->
            setContent {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = ArticleScreens.HomeScreen.route
                ) {

                    // Home Screen
                    composable(route = ArticleScreens.HomeScreen.route) {
                        ArticleListScreen(articleList, viewModel, navController)
                    }

                    composable(
                        route = ArticleScreens.DetailScreen.route + "/{index}",
                        arguments = listOf(navArgument(name = "index") {
                            type = NavType.IntType
                        })
                    ) { entry ->
                        val index = entry.arguments?.getInt("index")

                        index?.let {
                            if (articleList is Result.Success) {
                                articleList.data[index]
                            } else {
                                null
                            }
                        }?.let {
                            ExpandedArticleScreen(navController, it)
                        }
                    }

                }
            }

        }
    }

}