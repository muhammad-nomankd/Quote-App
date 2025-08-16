package com.example.quoteapp.domain.repository

import com.example.quoteapp.data.local.model.Quote

interface QuoteRepository {
    suspend fun getRandomQuotes(): Result<List<Quote>>
    suspend fun getRandomQuote():Result<List<Quote>>
    suspend fun getFavoriteQuotes():Result<List<Quote>>
    suspend fun saveLocalQuote(quote: Quote)
    suspend fun toggleFavorite(quote: Quote)
    suspend fun isQuoteFavorite(quote: Quote): Boolean

    suspend fun deleteQuote(quote: Quote)
}