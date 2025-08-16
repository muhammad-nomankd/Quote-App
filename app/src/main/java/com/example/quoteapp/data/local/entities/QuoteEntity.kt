package com.example.quoteapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quoteapp.data.local.model.Quote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "favorite_quotes")
data class QuoteEntity(
    @PrimaryKey val id: String, val content: String, val author: String, val tags: String
)

fun QuoteEntity.toQuote(): Quote {
    return Quote(
        id = id,
        content = content,
        author = author,
        tags = Gson().fromJson(tags, object : TypeToken<List<String>>() {}.type),
        isFavorite = true
    )
}

fun Quote.toEntity(): QuoteEntity {
    return QuoteEntity(
        id = id, content = content, author = author, tags = Gson().toJson(tags)
    )
}
