package com.example.stockapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stockapp.data.dao.StockCardDao
import com.example.stockapp.data.model.StockCardModel

@Database(entities = [StockCardModel::class], version = 1, exportSchema = false)
abstract class StockDatabase : RoomDatabase() {
    abstract fun stockCardDao(): StockCardDao
}