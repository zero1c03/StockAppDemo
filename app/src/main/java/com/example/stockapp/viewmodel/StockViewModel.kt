package com.example.stockapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.data.dao.StockCardDao
import com.example.stockapp.data.model.BwibbuModel
import com.example.stockapp.data.model.StockCardModel
import com.example.stockapp.data.model.StockDayAvgModel
import com.example.stockapp.data.model.StockDayModel
import com.example.stockapp.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val repository: StockRepository,
    private val stockCardDao: StockCardDao
) : ViewModel() {


    private val _initialized = mutableStateOf(false)
    val initialized: State<Boolean> = _initialized

    private val _showSortBottomSheet = mutableStateOf(false)
    val showSortBottomSheet: State<Boolean> = _showSortBottomSheet

    private val _showStockRatioDialog = mutableStateOf(false)
    val showStockRatioDialog: State<Boolean> = _showStockRatioDialog

    private val _fetchingData = mutableStateOf(false)
    val fetchingData: State<Boolean> = _fetchingData

    private val _noDataAvailable = mutableStateOf(true)
    val noDataAvailable: State<Boolean> = _noDataAvailable

    private val _stockCardData = MutableStateFlow<List<StockCardModel>>(emptyList())
    val stockCardData: StateFlow<List<StockCardModel>> = _stockCardData.asStateFlow()

    private val _showStockRatioDialogData = MutableStateFlow<StockCardModel>(StockCardModel())
    val showStockRatioDialogData: StateFlow<StockCardModel> = _showStockRatioDialogData

    fun setInitialized(value: Boolean) {
        _initialized.value = value
    }

    fun showAllStockData(isNetworkAvailable: Boolean) {
        _fetchingData.value = true
        if (isNetworkAvailable) {
            fetchAllStockData()
        } else {
            fetchAllStockDataFromDatabase()
        }
    }

    private fun fetchAllStockData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val allStockData = repository.fetchAllStockData()
                val combinedData = combineStockData(
                    Triple(
                        allStockData.first.toList(),
                        allStockData.second.toList(),
                        allStockData.third.toList()
                    )
                )
                updateStockData(combinedData)
            } catch (_: Exception) {
                _noDataAvailable.value = true
            } finally {
                _fetchingData.value = false
            }
        }
    }

    private fun fetchAllStockDataFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = stockCardDao.getAllStocks()
                updateStockData(data)
            } catch (_: Exception) {
                _noDataAvailable.value = true
            } finally {
                _fetchingData.value = false
            }
        }
    }

    private fun combineStockData(allStockData: Triple<List<BwibbuModel>, List<StockDayAvgModel>, List<StockDayModel>>): List<StockCardModel> {
        val stockData = allStockData.first
        val stockDayAvgData = allStockData.second
        val stockDayData = allStockData.third

        return stockData.mapNotNull { stock ->
            val dayData = stockDayData.find { it.code == stock.code }
            val avgData = stockDayAvgData.find { it.code == stock.code }
            if (dayData != null && avgData != null) {
                StockCardModel(
                    code = stock.code,
                    name = stock.name,
                    openingPrice = dayData.openingPrice,
                    closingPrice = dayData.closingPrice,
                    highestPrice = dayData.highestPrice,
                    lowestPrice = dayData.lowestPrice,
                    change = dayData.change,
                    tradeVolume = dayData.tradeVolume,
                    transaction = dayData.transaction,
                    tradeValue = dayData.tradeValue,
                    peRatio = stock.peRatio,
                    dividendYield = stock.dividendYield,
                    pbRatio = stock.pbRatio,
                    monthlyAveragePrice = avgData.monthlyAveragePrice,
                )
            } else {
                null
            }
        }
    }

    private fun updateStockData(data: List<StockCardModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (data.isEmpty()) {
                _noDataAvailable.value = true
            } else {
                _noDataAvailable.value = false
                stockCardDao.insertStock(data)
                _stockCardData.value = data
            }
        }
    }

    fun sortStockData(ascending: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val sortedData = if (ascending) {
                _stockCardData.value.sortedBy { it.code.toInt() }
            } else {
                _stockCardData.value.sortedByDescending { it.code.toInt() }
            }
            _stockCardData.value = sortedData
            _fetchingData.value = false
        }
    }

    fun showSortBottomSheet(show: Boolean) {
        _showSortBottomSheet.value = show
    }

    fun showStockRatioDialog(show: Boolean, showStockRatioDialogData: StockCardModel) {
        _showStockRatioDialog.value = show
        _showStockRatioDialogData.value = showStockRatioDialogData
    }
}