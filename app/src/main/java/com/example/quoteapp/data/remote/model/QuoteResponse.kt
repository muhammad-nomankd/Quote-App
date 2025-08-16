package com.example.quoteapp.data.remote.model

import com.example.quoteapp.data.local.model.Quote
import java.util.UUID

data class QuoteResponse(
    val q: String = "Click generate to get a quote",
    val a: String = "Unknown"
)

fun QuoteResponse.toQuote(): Quote {
    return Quote(
        id = UUID.randomUUID().toString(),
        content = q,
        author = a,
        tags = emptyList()
    )
}
