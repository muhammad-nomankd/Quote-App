package com.example.quoteapp.data.local.model

data class Quote(
    val id: String,
    val content: String= "Click generate to get a quote",
    val author: String = "Author",
    var isFavorite: Boolean = false,
    val tags: List<String>
)