package com.example.stockapp.data.model

import com.google.gson.annotations.SerializedName

data class BwibbuModel(
    @SerializedName("Code")
    val code: String,           // 股票代號
    @SerializedName("Name")
    val name: String,           // 股票名稱
    @SerializedName("PEratio")
    val peRatio: String,        // 本益比
    @SerializedName("DividendYield")
    val dividendYield: String,  // 殖利率(%)
    @SerializedName("PBratio")
    val pbRatio: String         // 股價淨值比
)