package com.example.quoteapp.di

import com.example.quoteapp.data.remote.QuoteApi
import com.example.quoteapp.data.repository.QuoteRepositoryImp
import com.example.quoteapp.domain.repository.QuoteRepository
import com.example.quoteapp.data.local.QuoteDao
import com.example.quoteapp.data.local.QuoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuoteApi(): QuoteApi {
        return Retrofit.Builder()
            .baseUrl(QuoteApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
            )
            .build()
            .create(QuoteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQuoteDatabase(@ApplicationContext context: Context): QuoteDatabase {
        return Room.databaseBuilder(
            context,
            QuoteDatabase::class.java,
            "quote_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideQuoteDao(quoteDatabase: QuoteDatabase): QuoteDao {
        return quoteDatabase.quoteDao
    }
    @Provides
    @Singleton
    fun provideQuoteRepository(api: QuoteApi, dao: QuoteDao): QuoteRepository {
        return QuoteRepositoryImp(
            api,
            dao
        )
    }
}