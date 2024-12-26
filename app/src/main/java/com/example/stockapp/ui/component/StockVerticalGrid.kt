package com.example.stockapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockapp.viewmodel.StockViewModel

@Composable
fun StockVerticalGrid(stockViewModel: StockViewModel, paddingValues: PaddingValues) {
    val stockDataState = stockViewModel.stockCardData.collectAsState()

    LazyVerticalGrid(
        modifier = Modifier
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.primaryContainer),
        columns = GridCells.Adaptive(300.dp),
    ) {
        items(count = stockDataState.value.size) { index ->
            StockCard(stockViewModel, stockDataState.value[index])
        }
    }
}
