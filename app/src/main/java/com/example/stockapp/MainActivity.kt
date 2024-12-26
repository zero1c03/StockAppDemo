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


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            StockAppTheme(darkTheme = themeViewModel.isDarkTheme) {
                StockListScreen(onToggleTheme = { themeViewModel.toggleTheme() })
            }
        }
    }
}

