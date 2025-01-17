package com.example.stockapp.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * ViewModel for managing the theme state of the application.
 */
@HiltViewModel
class ThemeViewModel @Inject constructor() : ViewModel() {

    // MutableStateFlow to hold the current theme state (true for dark theme, false for light theme).
    private val _isDarkTheme = MutableStateFlow(false)

    // Public getter for the current theme state.
    val isDarkTheme: Boolean get() = _isDarkTheme.value

    /**
     * Toggles the theme between dark and light mode.
     */
    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
}