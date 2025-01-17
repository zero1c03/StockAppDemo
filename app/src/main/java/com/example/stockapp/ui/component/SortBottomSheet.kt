package com.example.stockapp.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockapp.viewmodel.StockViewModel

/**
 * A composable function that displays a bottom sheet for sorting stock data.
 *
 * @param stockViewModel The ViewModel that manages the stock data and UI state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(stockViewModel: StockViewModel) {

    // State for managing the visibility of the bottom sheet
    val bottomSheetState = rememberModalBottomSheetState(false)

    // Check if the bottom sheet should be shown
    if (stockViewModel.showSortBottomSheet.value) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = { stockViewModel.showSortBottomSheet(false) }
        ) {
            Column {
                // Title text for the sorting options
                Text(
                    text = "Sort by",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                // List item for sorting in ascending order
                ListItem(
                    headlineContent = {
                        Text(
                            text = "Ascending",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier.clickable(enabled = true, onClick = {
                        stockViewModel.sortStockData(ascending = true)
                        stockViewModel.showSortBottomSheet(false)
                    })
                )
                // List item for sorting in descending order
                ListItem(
                    headlineContent = {
                        Text(
                            text = "Descending",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier.clickable(enabled = true, onClick = {
                        stockViewModel.sortStockData(ascending = false)
                        stockViewModel.showSortBottomSheet(false)
                    })
                )
            }
        }
    }
}