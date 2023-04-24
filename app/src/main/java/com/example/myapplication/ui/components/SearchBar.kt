package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.myapplication.NewsOptions

@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFilter: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    NewsOptions.query = searchText
    TextField(
        value = searchText,
        onValueChange = { newValue: String -> searchText = newValue; NewsOptions.query = newValue },
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
                onClick = { searchText = "" }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    tint = Color.Black,
                    contentDescription = "Clear",
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        }
    )

    Spacer(modifier = Modifier.width(5.dp))
    IconButton(
        onClick = onFilter
    ) {
        Icon(
            imageVector = Icons.Filled.List,
            tint = Color.White,
            contentDescription = "Filter",
            modifier = Modifier
                .size(50.dp)
        )
    }
}