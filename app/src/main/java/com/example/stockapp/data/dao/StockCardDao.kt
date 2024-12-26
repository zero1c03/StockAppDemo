package com.example.stockapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stockapp.data.model.StockCardModel

@Dao
interface StockCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stockCard: List<StockCardModel>)

    @Query("SELECT * FROM stock_card")
    suspend fun getAllStocks(): List<StockCardModel>
}