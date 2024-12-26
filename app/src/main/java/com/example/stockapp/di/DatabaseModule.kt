package com.example.stockapp.di

import android.content.Context
import androidx.room.Room
import com.example.stockapp.data.dao.StockCardDao
import com.example.stockapp.data.database.StockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StockDatabase {
        return Room.databaseBuilder(
            context,
            StockDatabase::class.java,
            "stock_database"
        ).build()
    }

    @Provides
    fun provideStockCardDao(database: StockDatabase): StockCardDao {
        return database.stockCardDao()
    }
}