package com.example.quoteapp.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteapp.data.local.model.Quote
import com.example.quoteapp.domain.usecases.DeleteQuoteUseCase
import com.example.quoteapp.domain.usecases.GetFavoriteQuotesUseCase
import com.example.quoteapp.domain.usecases.GetRandomQuoteUseCase
import com.example.quoteapp.domain.usecases.SaveLocalQuoteUseCase
import com.example.quoteapp.domain.usecases.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val getRandomQuoteUseCase: GetRandomQuoteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val loadFavoriteQuotesUseCase: GetFavoriteQuotesUseCase,
    private val saveLocalQuoteUseCase: SaveLocalQuoteUseCase,
    private val deleteQuoteUseCase: DeleteQuoteUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()




    fun getNewQuote() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = getRandomQuoteUseCase()
            _uiState.value = result.fold(onSuccess = { quoteList ->
                val quote = quoteList.first()
                UiState(currentQuote = quote, isLoading = false)
            }, onFailure = { error ->
                UiState(isLoading = false, errorMessage = error.message.toString())
            })
        }
    }

    fun loadFavoriteQuotes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = loadFavoriteQuotesUseCase()
            _uiState.update { currentState ->
                result.fold(onSuccess = { quotes ->
                    currentState.copy(isLoading = false, quoteList = quotes, errorMessage = null)
                }, onFailure = { error ->
                    currentState.copy(isLoading = false, errorMessage = error.message)
                })
            }
        }
    }

    fun saveLocalQuote(quote: Quote) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            saveLocalQuoteUseCase(quote)
            _uiState.value = _uiState.value.copy(isLoading = false)

            loadFavoriteQuotes()
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentQuote = _uiState.value.currentQuote
            Log.d("Debug", "Before toggle - Quote ID: ${currentQuote.id}, isFavorite: ${currentQuote.isFavorite}")

            val newIsFavorite = !currentQuote.isFavorite
            Log.d("Debug", "Will set isFavorite to: $newIsFavorite")

            toggleFavoriteUseCase(currentQuote.copy(isFavorite = newIsFavorite))

            _uiState.update {
                it.copy(currentQuote = currentQuote.copy(isFavorite = newIsFavorite))
            }

            Log.d("Debug", "After toggle - UI state isFavorite: ${_uiState.value.currentQuote.isFavorite}")
            loadFavoriteQuotes()
        }
    }


    fun shareQuote(context: Context) {
        val quote = _uiState.value.currentQuote

        val shareText = "\"${quote.content}\" - ${quote.author}"
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Quote"))
    }

    fun deleteQuote(quote: Quote) {
        viewModelScope.launch {
            try {
                deleteQuoteUseCase(quote)
                _uiState.update { currentState ->
                    val updatedList = currentState.quoteList?.filter { it.id != quote.id }
                    currentState.copy(quoteList = updatedList)
                }

                Log.d("DeleteQuote", "Quote ${quote.id} deleted successfully")
            } catch (e: Exception) {
                Log.e("DeleteQuote", "Error deleting quote: ${e.message}")
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = "Failed to delete quote")
                }
            }
        }
    }
}