package com.example.stockapp.data.repository

import com.example.stockapp.data.model.BwibbuModel
import com.example.stockapp.data.model.StockDayAvgModel
import com.example.stockapp.data.model.StockDayModel
import com.example.stockapp.network.StockApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository class for managing stock data operations.
 *
 * @property stockApi The API service for fetching stock data.
 */
open class StockRepository @Inject constructor(private val stockApi: StockApi) {

    /**
     * Fetches stock data for testing purposes.
     *
     * @return A list of BwibbuModel containing stock data.
     */
    suspend fun getStockData(): List<BwibbuModel> = stockApi.getStockInfo()

    /**
     * Fetches stock day average data for testing purposes.
     *
     * @return A list of StockDayAvgModel containing stock day average data.
     */
    suspend fun getStockDayAvgData(): List<StockDayAvgModel> = stockApi.getStockDayAvgInfo()

    /**
     * Fetches stock day data for testing purposes.
     *
     * @return A list of StockDayModel containing stock day data.
     */
    suspend fun getStockDayData(): List<StockDayModel> = stockApi.getStockDayInfo()

    /**
     * Fetches all stock data including stock, stock day average, and stock day data.
     *
     * @return A Triple containing sequences of BwibbuModel, StockDayAvgModel, and StockDayModel.
     */
    suspend fun fetchAllStockData(): Triple<Sequence<BwibbuModel>, Sequence<StockDayAvgModel>, Sequence<StockDayModel>> {
        return withContext(Dispatchers.IO) {
            val stockData = stockApi.getStockInfo().asSequence()
            val stockDayAvgData = stockApi.getStockDayAvgInfo().asSequence()
            val stockDayData = stockApi.getStockDayInfo().asSequence()
            Triple(stockData, stockDayAvgData, stockDayData)
        }
    }
}