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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(stockViewModel: StockViewModel) {

    val bottomSheetState = rememberModalBottomSheetState(false)

    if (stockViewModel.showSortBottomSheet.value) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = { stockViewModel.showSortBottomSheet(false) }
        ) {
            Column {
                Text(
                    text = "Sort by",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
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