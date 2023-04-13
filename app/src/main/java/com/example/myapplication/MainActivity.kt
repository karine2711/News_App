package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.models.Article


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadNews()
        viewModel.articles.observe(this) { articleList ->
            setContent {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column() {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(color = Color(0xFF011b45))
                        ) {
                            Column() {
                                Text(
                                    text = "News ",
                                    style = MaterialTheme.typography.h2,
                                    color = Color.White,
                                    modifier = Modifier.padding(15.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .fillMaxWidth()
                                ) {
                                    SearchBar(
                                        onSearch = { searchText ->
                                        }, modifier = Modifier
                                            .weight(1f)
                                            .background(Color.White)
                                    )
                                }
                            }
                        }
                        ArticleList(articleList)
                    }

                }
            }

        }
    }
}

@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }

    TextField(
        value = searchText,
        onValueChange = { newValue: String -> searchText = newValue },
        modifier = modifier,
        placeholder = { Text("Search") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(searchText) }
        ),
        leadingIcon = {
            IconButton(
                onClick = { onSearch(searchText) },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Filled.List,
                    tint = Color.Black,
                    contentDescription = "Filter",
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        }
    )
}

@Composable
fun ArticleList(articleResult: Result<List<Article>>) {
    when (articleResult) {
        is Result.Success -> {
            val articles = articleResult.data
            LazyColumn {
                items(articles) { article ->
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .height(140.dp)
                            .fillMaxWidth(),
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
                                Text(
                                    text = article.author,
                                    style = MaterialTheme.typography.subtitle2,
                                )
                            }
                        }
                    }
                }
            }
        }
        is Result.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    style = MaterialTheme.typography.h2,
                    color = Color.Red,
                    text = "Something went wrong!"
                )
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
}