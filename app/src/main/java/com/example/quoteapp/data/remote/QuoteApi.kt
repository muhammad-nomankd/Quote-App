package com.example.quoteapp.data.remote

import com.example.quoteapp.data.remote.model.QuoteResponse
import retrofit2.http.GET

interface QuoteApi {

    @GET("random")
    suspend fun getRandomQuote(): List<QuoteResponse>

    companion object {
        const val BASE_URL = "https://zenquotes.io/api/"
    }
}