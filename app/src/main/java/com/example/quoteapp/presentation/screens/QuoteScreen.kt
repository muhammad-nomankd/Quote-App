package com.example.quoteapp.presentation.screens

import android.R.attr.duration
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quoteapp.presentation.QuoteViewModel
import com.example.quoteapp.ui.theme.backgroundColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuoteScreen(
    viewModel: QuoteViewModel = hiltViewModel(),
    onShareQuote: () -> Unit,
    onToggleFavorite: () -> Unit,
    onNavigateToAddQuote: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val verticalScroll = rememberScrollState()
    val configuration = LocalConfiguration.current
    val portrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val scope = rememberCoroutineScope()
    val snackBarHostState = SnackbarHostState()

    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("Favorite", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    )

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor), snackbarHost = {
        SnackbarHost(hostState = snackBarHostState) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                shape = RoundedCornerShape(12.dp),
                actionColor = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }, bottomBar = {
        BottomNavigationBar(
            items = items,
            selectedItemIndex = selectedItem,
            onItemSelected = { selectedItem = it })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { onNavigateToAddQuote() }) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .then(
                    if (portrait) Modifier.padding(32.dp) else Modifier.padding(8.dp)
                ), contentAlignment = Alignment.Center
        ) {
            uiState.errorMessage.let { error ->
                if (error == null){
                    return@let
                }
                LaunchedEffect(error) {
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            error.toString(),
                            actionLabel = "cancel",
                            duration = SnackbarDuration.Short
                        )
                    }
                    delay(1500)
                    snackBarHostState.currentSnackbarData?.dismiss()
                }
            }

            when (selectedItem) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .verticalScroll(verticalScroll)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,

                        ) {
                        Text(
                            "Daily Inspiration",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground)
                        )
                        Text(
                            "Discover wisdom from great minds",
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                if (uiState.isLoading) {
                                    CircularProgressIndicator()
                                    Spacer(modifier = Modifier.height(48.dp))
                                    Text(
                                        "Finding inspiration...",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                } else {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            uiState.currentQuote.content,
                                            style = MaterialTheme.typography.bodyLarge,
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            "— ${uiState.currentQuote.author}",
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.labelMedium
                                        )

                                        Spacer(Modifier.height(16.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            IconButton(onClick = {
                                                if (uiState.currentQuote.content.isNullOrEmpty()){
                                                    return@IconButton
                                                }


                                                onShareQuote() }) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Share,
                                                    contentDescription = "share quote",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                            IconButton(onClick = {
                                                scope.launch {
                                                    if (uiState.currentQuote.content.isNullOrEmpty()){
                                                        return@launch
                                                    }
                                                    onToggleFavorite()
                                                }

                                            }) {
                                                Icon(
                                                    imageVector = if (uiState.currentQuote.isFavorite) Icons.Filled.Favorite
                                                    else Icons.Outlined.FavoriteBorder,
                                                    contentDescription = "toggle favorite",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }


                                    }
                                }
                            }


                        }
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.getNewQuote() }) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Icon(Icons.Outlined.Refresh, contentDescription = "Refresh")
                                }
                                Spacer(Modifier.width(8.dp))
                                Text("Generate New Quote")
                            }
                        }
                    }

                }

                1 -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            "Favorite Quotes",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground),

                            )
                        QuoteListScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>, selectedItemIndex: Int, onItemSelected: (Int) -> Unit
) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            val isSelected = selectedItemIndex == index
            NavigationBarItem(
                selected = isSelected, onClick = { onItemSelected(index) }, icon = {
                Icon(
                    imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                    contentDescription = item.label,
                    modifier = Modifier.size(26.dp)
                )
            }, label = {
                Text(item.label)
            }, alwaysShowLabel = true, colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = Color.Gray,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = Color.Gray,
            )
            )
        }
    }
}

data class BottomNavItem(
    val label: String, val filledIcon: ImageVector, val outlinedIcon: ImageVector
)
