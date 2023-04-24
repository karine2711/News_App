package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.NewsOptions
import com.example.myapplication.models.Filter
import com.example.myapplication.ui.theme.Color.Companion.DarkBlue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterBottomSheet(
    filters: List<Filter>,
    onFilterSelected: (Filter?) -> Unit,
    sheetState: ModalBottomSheetState,
    followupContent: @Composable () -> Unit
) {
    var currentFilter: Filter? by remember { mutableStateOf(null) }
    NewsOptions.category = currentFilter?.name
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Select a filter",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                filters.forEach { filter ->
                    FilterOption(
                        filter = filter,
                        bgColor = if (currentFilter == filter) Color.Blue else DarkBlue
                    ) { currentFilter = filter }
                }
                Row(modifier = Modifier.padding(2.dp)) {
                    Button(
                        onClick = { onFilterSelected(currentFilter) },
                        modifier = Modifier
                            .padding(3.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue)
                    ) { Text(text = "Apply", color = Color.White) }
                    Button(
                        onClick = { currentFilter = null }, modifier = Modifier
                            .padding(3.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue)
                    ) { Text(text = "Clear", color = Color.White) }
                }
            }
        }
    ) { followupContent() }
}

@Composable
fun FilterOption(
    filter: Filter,
    bgColor: Color,
    onClick: (Filter) -> Unit
) {
    Button(
        onClick = { onClick(filter) },

        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = bgColor)
    ) {
        Text(
            color = Color.White,
            text = filter.name
        )
    }
}
