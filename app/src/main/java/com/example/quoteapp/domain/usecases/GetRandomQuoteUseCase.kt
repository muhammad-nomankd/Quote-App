package com.example.quoteapp.domain.usecases

import com.example.quoteapp.data.local.model.Quote
import com.example.quoteapp.domain.repository.QuoteRepository
import javax.inject.Inject

class GetRandomQuoteUseCase @Inject constructor(private val quoteRepository: QuoteRepository) {
    suspend operator fun invoke(): Result<List<Quote>>{
        return quoteRepository.getRandomQuote()
    }
}