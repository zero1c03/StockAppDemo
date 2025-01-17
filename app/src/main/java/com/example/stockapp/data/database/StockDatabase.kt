package com.example.stockapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stockapp.data.dao.StockCardDao
import com.example.stockapp.data.model.StockCardModel

/**
 * The Room database for this app.
 * This database contains the StockCardModel entity and provides a DAO for accessing stock card data.
 *
 * @property stockCardDao The DAO for accessing stock card data.
 */
@Database(entities = [StockCardModel::class], version = 1, exportSchema = false)
abstract class StockDatabase : RoomDatabase() {
    /**
     * Provides the DAO for accessing stock card data.
     *
     * @return The StockCardDao instance.
     */
    abstract fun stockCardDao(): StockCardDao
}