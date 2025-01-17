package com.example.stockapp.network

import com.example.stockapp.data.model.BwibbuModel
import com.example.stockapp.data.model.StockDayAvgModel
import com.example.stockapp.data.model.StockDayModel
import retrofit2.http.GET

/**
 * Interface for accessing stock data from the API.
 */
interface StockApi {

    /**
     * Fetches stock information.
     *
     * @return A list of BwibbuModel containing stock information.
     */
    @GET("exchangeReport/BWIBBU_ALL")
    suspend fun getStockInfo(): List<BwibbuModel>

    /**
     * Fetches stock day average information.
     *
     * @return A list of StockDayAvgModel containing stock day average information.
     */
    @GET("exchangeReport/STOCK_DAY_AVG_ALL")
    suspend fun getStockDayAvgInfo(): List<StockDayAvgModel>

    /**
     * Fetches stock day information.
     *
     * @return A list of StockDayModel containing stock day information.
     */
    @GET("exchangeReport/STOCK_DAY_ALL")
    suspend fun getStockDayInfo(): List<StockDayModel>
}