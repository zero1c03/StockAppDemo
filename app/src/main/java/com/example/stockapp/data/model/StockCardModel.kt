package com.example.stockapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "stock_card")
data class StockCardModel(
    @PrimaryKey val code: String = "證券代號", // 證券代號
    val name: String = "證券名稱", // 證券名稱
    val openingPrice: String = "開盤價", // 開盤價
    val closingPrice: String = "收盤價", // 收盤價
    val highestPrice: String = "最高價", // 最高價
    val lowestPrice: String = "最低價", // 最低價
    val change: String = "漲跌價差", // 漲跌價差
    val tradeVolume: String = "成交股數", // 成交股數
    val transaction: String = "成交筆數", // 成交筆數
    val tradeValue: String = "成交金額", // 成交金額
    val peRatio: String = "本益比", // 本益比
    val dividendYield: String = "殖利率(%)", // 殖利率(%)
    val pbRatio: String = "股價淨值比", // 股價淨值比
    val monthlyAveragePrice: String = "月平均價", // 月平均價
)
