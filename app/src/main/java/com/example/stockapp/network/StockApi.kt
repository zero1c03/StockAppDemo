package com.example.stockapp.network

import com.example.stockapp.data.model.BwibbuModel
import com.example.stockapp.data.model.StockDayAvgModel
import com.example.stockapp.data.model.StockDayModel
import retrofit2.http.GET

interface StockApi {
    @GET("exchangeReport/BWIBBU_ALL")
    suspend fun getStockInfo(): List<BwibbuModel>

    @GET("exchangeReport/STOCK_DAY_AVG_ALL")
    suspend fun getStockDayAvgInfo(): List<StockDayAvgModel>

    @GET("exchangeReport/STOCK_DAY_ALL")
    suspend fun getStockDayInfo(): List<StockDayModel>

}