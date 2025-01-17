package com.example.stockapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.StockAppTheme
import com.example.stockapp.ui.screen.StockListScreen
import com.example.stockapp.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity of the StockApp.
 * This activity sets up the content view and initializes the theme and stock list screen.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Obtain the ThemeViewModel instance using Hilt.
            val themeViewModel: ThemeViewModel = hiltViewModel()
            // Set the content view with the StockAppTheme and StockListScreen.
            StockAppTheme(darkTheme = themeViewModel.isDarkTheme) {
                StockListScreen(onToggleTheme = { themeViewModel.toggleTheme() })
            }
        }
    }
}