package com.example.stockapp.ui.screen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stockapp.ui.component.ShowStockRatioDialog
import com.example.stockapp.ui.component.SortBottomSheet
import com.example.stockapp.ui.component.StockVerticalGrid
import com.example.stockapp.viewmodel.StockViewModel

/**
 * Composable function that displays the stock list screen.
 *
 * @param stockViewModel The ViewModel that manages the stock data.
 * @param onToggleTheme Callback function to toggle the theme.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    stockViewModel: StockViewModel = hiltViewModel(),
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val initialized = stockViewModel.initialized.value

    // Launches a coroutine that initializes the stock data if not already initialized.
    LaunchedEffect(Unit) {
        if (!initialized) {
            stockViewModel.showAllStockData(isNetworkAvailable(context))
            stockViewModel.setInitialized(true)
        }
    }

    val isLoading = stockViewModel.fetchingData.value
    val noDataAvailable = stockViewModel.noDataAvailable.value
    val tintColor =
        if (!isLoading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline

    // Scaffold layout with a top app bar and content.
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Stock Info",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
                    // Refresh button to reload stock data.
                    IconButton(onClick = {
                        stockViewModel.showAllStockData(
                            isNetworkAvailable(
                                context
                            )
                        )
                    }, enabled = !isLoading) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = tintColor
                        )
                    }
                    // Menu button to show sort options.
                    IconButton(
                        onClick = { stockViewModel.showSortBottomSheet(true) },
                        enabled = !isLoading
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Toggle Theme",
                            tint = tintColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        // Displays a loading indicator if data is being fetched.
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        // Displays a message if no data is available.
        } else if (noDataAvailable) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No data available,\nplease check your network connection.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        // Displays the stock data in a grid.
        } else {
            StockVerticalGrid(stockViewModel, paddingValues)
        }
    }
    SortBottomSheet(stockViewModel)
    ShowStockRatioDialog(stockViewModel)
}

/**
 * Checks if the network is available.
 *
 * @param context The context used to get the ConnectivityManager.
 * @return True if the network is available, false otherwise.
 */
private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}