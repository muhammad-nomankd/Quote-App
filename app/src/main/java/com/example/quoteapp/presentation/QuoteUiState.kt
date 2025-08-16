package com.example.quoteapp.presentation

import com.example.quoteapp.data.local.model.Quote

data class UiState(
    val currentQuote: Quote = Quote(
        "", "", "",
        isFavorite = false,
        tags = emptyList()
    ),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val quoteList:List<Quote>? = emptyList<Quote>()
)