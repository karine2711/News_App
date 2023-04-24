package com.example.myapplication.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

import com.example.myapplication.models.Article
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpandedArticleScreen(navController: NavController, article: Article) {
    val outputFormat = SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a", Locale.getDefault())
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    Scaffold(topBar = {
        TopAppBar(backgroundColor = Color.Transparent, elevation = 0.dp) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Arrow Back",
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Read more", fontWeight = FontWeight.Bold)
            }
        }
    }) {
        it
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
              Text(text = article.title, style = MaterialTheme.typography.h4)


            AsyncImage(
                model = article.urlToImage,
                contentDescription = "avatar",
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(text = article.description, style = MaterialTheme.typography.subtitle1)

            inputFormat.parse(article.publishedAt)
                ?.let { pb -> Text(text = "Published at: ${outputFormat.format(pb)}") }

            article.author?.let { a -> Text(text = "Author: $a") }
            Text(text = "Source: ${article.source.name}")


            Divider(Modifier.fillMaxWidth())
            Text(text = article.content)
            val uriHandler = LocalUriHandler.current
            Text(
                text = "Read More",
                modifier = Modifier.clickable(
                    onClick = {
                        uriHandler.openUri(article.url)
                    }
                ),
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

