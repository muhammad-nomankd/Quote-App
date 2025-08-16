package com.example.quoteapp.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.quoteapp.data.local.entities.QuoteEntity

@Dao
interface QuoteDao {
    @Query("SELECT * FROM favorite_quotes ORDER BY id DESC")
    suspend fun getAllFavorites(): List<QuoteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Delete
    suspend fun deleteQuote(quote: QuoteEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_quotes WHERE id = :quoteId)")
    suspend fun isQuoteFavorite(quoteId: String): Boolean

    @Query("SELECT * FROM favorite_quotes WHERE id = :quoteId")
    suspend fun getQuoteById(quoteId: String): QuoteEntity?
}

@Database(
    entities = [QuoteEntity::class], version = 1, exportSchema = false
)

abstract class QuoteDatabase : RoomDatabase() {
    abstract val quoteDao: QuoteDao
}