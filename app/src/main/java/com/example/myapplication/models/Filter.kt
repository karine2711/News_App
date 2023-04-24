package com.example.myapplication.models

data class Filter(
    val id: Int,
    val name: String
)

object DefaultFilters {
    val list = listOf(Filter(1, "Business"),
        Filter(2, "Entertainment"),
        Filter(3, "General"),
        Filter(4, "Health"))
}