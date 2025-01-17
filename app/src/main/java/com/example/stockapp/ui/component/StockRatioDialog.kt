package com.example.stockapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockapp.viewmodel.StockViewModel

/**
 * Displays a dialog showing stock ratios such as PE ratio, dividend yield, and PB ratio.
 *
 * @param stockViewModel The ViewModel that provides the data and state for the dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowStockRatioDialog(stockViewModel: StockViewModel) {
    if (stockViewModel.showStockRatioDialog.value) {
        val stockCardModel = stockViewModel.showStockRatioDialogData.collectAsState().value
        AlertDialog(
            onDismissRequest = { stockViewModel.showStockRatioDialog(false, stockCardModel) },
            text = {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("本益比")
                        Text(text = if (stockCardModel.peRatio.isEmpty()) "N/A" else stockCardModel.peRatio)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("殖利率")
                        Text(text = if (stockCardModel.dividendYield.isEmpty()) "N/A" else "${stockCardModel.dividendYield}%")
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("股價淨值比")
                        Text(text = if (stockCardModel.pbRatio.isEmpty()) "N/A" else stockCardModel.pbRatio)
                    }
                }
            },
            confirmButton = {
                Button(onClick = { stockViewModel.showStockRatioDialog(false, stockCardModel) }) {
                    Text("Close")
                }
            }
        )
    }
}