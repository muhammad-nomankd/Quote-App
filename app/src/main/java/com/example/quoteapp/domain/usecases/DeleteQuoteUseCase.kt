package com.example.quoteapp.domain.usecases

import com.example.quoteapp.data.local.model.Quote
import com.example.quoteapp.domain.repository.QuoteRepository
import jakarta.inject.Inject

class DeleteQuoteUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    suspend operator fun invoke(quote: Quote) {
        repository.deleteQuote(quote)
    }
}