package com.example.stockapp.data.repository

import com.example.stockapp.data.model.BwibbuModel
import com.example.stockapp.data.model.StockDayAvgModel
import com.example.stockapp.data.model.StockDayModel
import com.example.stockapp.network.StockApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class StockRepository @Inject constructor(private val stockApi: StockApi) {
    suspend fun getStockData(): List<BwibbuModel> = stockApi.getStockInfo()

    suspend fun getStockDayAvgData(): List<StockDayAvgModel> = stockApi.getStockDayAvgInfo()

    suspend fun getStockDayData(): List<StockDayModel> = stockApi.getStockDayInfo()

    suspend fun fetchAllStockData(): Triple<Sequence<BwibbuModel>, Sequence<StockDayAvgModel>, Sequence<StockDayModel>> {
        return withContext(Dispatchers.IO) {
            val stockData = stockApi.getStockInfo().asSequence()
            val stockDayAvgData = stockApi.getStockDayAvgInfo().asSequence()
            val stockDayData = stockApi.getStockDayInfo().asSequence()
            Triple(stockData, stockDayAvgData, stockDayData)
        }
    }
}