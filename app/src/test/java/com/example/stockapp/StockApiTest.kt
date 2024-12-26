package com.example.stockapp

import com.example.stockapp.network.StockApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(RobolectricTestRunner::class)
class StockApiTest {

    private lateinit var stockApi: StockApi

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.twse.com.tw/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        stockApi = retrofit.create(StockApi::class.java)
    }

    @Test
    fun testGetStockInfo() = runBlocking {
        val response = stockApi.getStockInfo()
        assertNotNull(response)
    }

    @Test
    fun testGetStockDayAvgInfo() = runBlocking {
        val response = stockApi.getStockDayAvgInfo()
        assertNotNull(response)
    }

    @Test
    fun testGetStockDayInfo() = runBlocking {
        val response = stockApi.getStockDayInfo()
        assertNotNull(response)
    }
}