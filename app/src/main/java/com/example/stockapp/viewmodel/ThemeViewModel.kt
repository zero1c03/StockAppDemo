package com.example.stockapp.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor() : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: Boolean get() = _isDarkTheme.value

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
}