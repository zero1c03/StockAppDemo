package com.example.stockapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.stockapp.data.model.StockCardModel

open class MockStockViewModel : ViewModel() {
    val mockStockCardData = mutableStateOf(
        listOf(
            StockCardModel(
                code = "0050",
                name = "台積電",
                openingPrice = "193.75",
                closingPrice = "192.60",
                highestPrice = "193.75",
                lowestPrice = "192.00",
                change = "-1.8000",
                transaction = "31645",
                tradeVolume = "12318276",
                tradeValue = "2374265742",
                peRatio = "20.00",
                dividendYield = "3.00",
                pbRatio = "5.00",
                monthlyAveragePrice = "600"
            )
        )
    )
}