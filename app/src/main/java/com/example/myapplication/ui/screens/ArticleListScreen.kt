package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.MainViewModel
import com.example.myapplication.Result
import com.example.myapplication.models.Article
import com.example.myapplication.models.DefaultFilters
import com.example.myapplication.ui.components.ArticleList
import com.example.myapplication.ui.components.FilterBottomSheet
import com.example.myapplication.ui.components.SearchBar
import com.example.myapplication.ui.theme.Color.Companion.DarkBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleListScreen(
    articleResult: Result<List<Article>>,
    viewModel: MainViewModel,
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val scope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden
        )
        var currentFilter: String? by remember { mutableStateOf(null) }
        FilterBottomSheet(
            filters = DefaultFilters.list,
            onFilterSelected = { filter ->
                scope.launch {
                    currentFilter = filter?.name
                    viewModel.getArticles()
                    sheetState.hide()
                }
            },
            sheetState = sheetState,
            followupContent = {
                Column() {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(color = DarkBlue)
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
                                    onSearch = { viewModel.getArticles() },
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(Color.White),
                                    { scope.launch { sheetState.show() } }
                                )
                            }
                        }
                    }
                    ArticleList(articleResult, viewModel, navController)
                }
            }
        )
    }

}