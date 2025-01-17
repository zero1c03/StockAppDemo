package com.example.stockapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stockapp.data.model.StockCardModel

/**
 * Data Access Object (DAO) interface for accessing stock card data.
 * This interface provides methods to insert and retrieve stock card data from the database.
 */
@Dao
interface StockCardDao {
    /**
     * Inserts a list of stock cards into the database.
     * If a conflict occurs, the existing data will be replaced.
     *
     * @param stockCard The list of stock cards to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stockCard: List<StockCardModel>)

    /**
     * Retrieves all stock cards from the database.
     *
     * @return A list of all stock cards.
     */
    @Query("SELECT * FROM stock_card")
    suspend fun getAllStocks(): List<StockCardModel>
}