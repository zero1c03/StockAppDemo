package com.example.stockapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.StockAppTheme
import com.example.stockapp.viewmodel.MockStockViewModel
import com.example.stockapp.data.model.StockCardModel
import com.example.stockapp.viewmodel.StockViewModel

/**
 * A composable function that displays a stock card with various stock information.
 *
 * @param stockViewModel The ViewModel that manages the stock data and UI state.
 * @param stockCardModel The model containing stock card data.
 */
@Composable
fun StockCard(stockViewModel: StockViewModel?, stockCardModel: StockCardModel) {
    val priceColor =
        if (stockCardModel.closingPrice.toFloat() > stockCardModel.monthlyAveragePrice.toFloat()) Color.Red else Color.Green
    val changeColor = if (stockCardModel.change.toFloat() > 0) Color.Red else Color.Green

    StockAppTheme {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.padding(8.dp),
            onClick = { stockViewModel?.showStockRatioDialog(true, stockCardModel) }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${stockCardModel.code} ${stockCardModel.name}",
                        style = MaterialTheme.typography.titleMedium.copy(lineHeight = 16.sp),
                    )

                    StockInfoRow(
                        priceColor,
                        "開盤價",
                        stockCardModel.openingPrice,
                        "收盤價",
                        stockCardModel.closingPrice
                    )
                    StockInfoRow(
                        MaterialTheme.colorScheme.onSurface,
                        "最高價",
                        stockCardModel.highestPrice,
                        "最低價",
                        stockCardModel.lowestPrice
                    )
                    StockInfoRow(
                        changeColor,
                        "漲跌價差",
                        stockCardModel.change,
                        "月平均價",
                        stockCardModel.monthlyAveragePrice
                    )
                    StockInfoRow(
                        MaterialTheme.colorScheme.onSurface,
                        "成交筆數",
                        stockCardModel.transaction,
                        "成交股數",
                        stockCardModel.tradeVolume
                    )
                    StockInfoRow(
                        MaterialTheme.colorScheme.onSurface,
                        "成交金額",
                        stockCardModel.tradeValue,
                        null,
                        null
                    )
                }
            }
        }
    }
}

/**
 * A composable function that displays a row of stock information.
 *
 * @param textColor The color of the text.
 * @param title1 The title of the first piece of information.
 * @param value1 The value of the first piece of information.
 * @param title2 The title of the second piece of information (optional).
 * @param value2 The value of the second piece of information (optional).
 */
@Composable
fun StockInfoRow(
    textColor: Color,
    title1: String,
    value1: String,
    title2: String?,
    value2: String?,
) {
    val originColorList = listOf("開盤價", "月平均價")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title1,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value1,
            style = MaterialTheme.typography.bodySmall,
            color = if (!originColorList.contains(title1)) textColor else MaterialTheme.colorScheme.onSurface,
            modifier = if (title2 != null) Modifier.weight(1f) else Modifier.weight(3f)
        )

        if (title2 == null || value2 == null) return
        Text(
            text = title2,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value2,
            style = MaterialTheme.typography.bodySmall,
            color = if (!originColorList.contains(title2)) textColor else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * A preview composable function for displaying a stock card.
 */
@Preview(showBackground = true)
@Composable
fun StockCardPreview() {
    val mockViewModel = MockStockViewModel()
    StockCard(
        stockViewModel = null,
        stockCardModel = mockViewModel.mockStockCardData.value[0]
    )
}