package com.example.quoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quoteapp.presentation.QuoteViewModel
import com.example.quoteapp.presentation.screens.AddQuoteScreen
import com.example.quoteapp.presentation.screens.QuoteScreen
import com.example.quoteapp.ui.theme.QuoteAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuoteAppTheme {
                val viewModel: QuoteViewModel = hiltViewModel()
                val navController = rememberNavController()
                val startDestination = "quote_screen"
                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable("quote_screen") {
                        QuoteScreen(
                            viewModel = viewModel,
                            onShareQuote = { viewModel.shareQuote(context) },
                            onToggleFavorite = { viewModel.toggleFavorite() },
                            onNavigateToAddQuote = {
                                navController.navigate("add_quote_screen")
                            }
                        )
                    }
                    composable("add_quote_screen") {
                        AddQuoteScreen(
                            onSave = { quote ->
                                scope.launch {
                                    viewModel.saveLocalQuote(quote)
                                    navController.popBackStack()
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
