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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    stockViewModel: StockViewModel = hiltViewModel(),
    onToggleTheme: () -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (!stockViewModel.initialized.value) {
            stockViewModel.showAllStockData(isNetworkAvailable(context))
            stockViewModel.setInitialized(true)
        }
    }

    var isLoading = stockViewModel.fetchingData.value
    val noDataAvailable = stockViewModel.noDataAvailable.value
    val tintColor =
        if (!isLoading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline

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
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
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

        } else {
            StockVerticalGrid(stockViewModel, paddingValues)
        }
    }
    SortBottomSheet(stockViewModel)
    ShowStockRatioDialog(stockViewModel)
}


private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}