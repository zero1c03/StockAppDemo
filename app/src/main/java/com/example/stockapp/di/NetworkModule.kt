package com.example.stockapp.di

import com.example.stockapp.network.StockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger module for providing network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://openapi.twse.com.tw/v1/"

    /**
     * Provides a singleton instance of Retrofit configured with the base URL and Gson converter.
     *
     * @return The Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Provides the StockApi service for accessing stock data.
     *
     * @param retrofit The Retrofit instance.
     * @return The StockApi instance.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): StockApi = retrofit.create(StockApi::class.java)
}