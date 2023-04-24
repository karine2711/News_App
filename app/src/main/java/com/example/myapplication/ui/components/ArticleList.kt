package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.MainViewModel
import com.example.myapplication.R
import com.example.myapplication.Result
import com.example.myapplication.models.Article
import com.example.myapplication.ui.screens.ArticleScreens


@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ArticleList(
    articleResult: Result<List<Article>>,
    viewModel: MainViewModel,
    navController: NavController
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.getArticles() })

    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        when (articleResult) {
            is Result.Success -> {
                val articles = articleResult.data
                LazyColumn {
                    itemsIndexed(articles) { index, article ->
                        ArticleCard(article) {
                            navController.navigate(ArticleScreens.DetailScreen.route + "/$index")
                        }
                    }
                }
            }
            is Result.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                    ) {
                    articleResult.exception.message?.let {
                        Text(
                            style = MaterialTheme.typography.h2,
                            color = Color.Red,
                            text = it
                        )
                    } ?: "Something went wrong!"
                }
            }
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(80.dp),
                        color = Color(0xFF011b45)
                    )
                }
            }
        }

        PullRefreshIndicator(
            isRefreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )

    }

}

@Composable
private fun ArticleCard(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(140.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(7.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFcacbcc)
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .width(140.dp)
                    .height(140.dp)
                    .background(Color.Red)
            ) {
                AsyncImage(
                    model = article.urlToImage,
                    contentDescription = article.title,
                    placeholder = painterResource(id = R.drawable.image6),
                    modifier = Modifier.fillMaxHeight(),
                    contentScale = ContentScale.FillHeight
                )
            }
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = article.source.name,
                    style = MaterialTheme.typography.subtitle1,
                )
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.body1,

                    )
                article.author?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.subtitle2,
                    )
                }
            }
        }
    }
}
