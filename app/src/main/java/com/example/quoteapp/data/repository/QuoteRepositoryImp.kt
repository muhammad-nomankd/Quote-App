package com.example.quoteapp.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.example.quoteapp.data.local.QuoteDao
import com.example.quoteapp.data.local.entities.toEntity
import com.example.quoteapp.data.local.entities.toQuote
import com.example.quoteapp.data.local.model.Quote
import com.example.quoteapp.data.remote.QuoteApi
import com.example.quoteapp.data.remote.model.toQuote
import com.example.quoteapp.domain.repository.QuoteRepository
import javax.inject.Inject
import retrofit2.HttpException

class QuoteRepositoryImp @Inject constructor(private val api: QuoteApi,private val dao: QuoteDao): QuoteRepository {
    override suspend fun getRandomQuotes(): Result<List<Quote>> {
        return try {
            val response = api.getRandomQuote().map { it.toQuote() }
            val quote = response
            Result.success(quote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getRandomQuote(): Result<List<Quote>> {
        return try {
            val response = api.getRandomQuote().map { it.toQuote() }
            Result.success(response)
        } catch (e: HttpException) {
            if (e.code() == 429) {
                Result.failure(Exception("Rate limit reached. Please wait and try again."))
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getFavoriteQuotes(): Result<List<Quote>> {
        return try {
            val quotes = dao.getAllFavorites().map { it.toQuote() } // New DAO method needed
            Result.success(quotes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveLocalQuote(quote: Quote) {
       dao.insertQuote(quote.toEntity())
    }

    override suspend fun toggleFavorite(quote: Quote) {
        if (quote.isFavorite) {
            dao.insertQuote(quote.toEntity())

        } else {
            dao.getQuoteById(quote.id)?.let { entity ->
                dao.deleteQuote(entity)
            }
        }
    }

    override suspend fun isQuoteFavorite(quote: Quote): Boolean {
        return dao.isQuoteFavorite(quote.id)
    }

    override suspend fun deleteQuote(quote: Quote) {
        dao.deleteQuote(quote.toEntity())
    }


}