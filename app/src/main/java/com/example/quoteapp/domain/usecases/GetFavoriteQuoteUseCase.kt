package com.example.quoteapp.domain.usecases

import com.example.quoteapp.data.local.model.Quote
import com.example.quoteapp.domain.repository.QuoteRepository
import jakarta.inject.Inject

class GetFavoriteQuotesUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    suspend operator fun invoke(): Result<List<Quote>> {
        return repository.getFavoriteQuotes()
    }
}