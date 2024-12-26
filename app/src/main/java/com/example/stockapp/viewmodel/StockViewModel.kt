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
import kotlinx.coroutines.flow.update
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

    fun fetchAllStockData() {
        viewModelScope.launch(Dispatchers.IO) {
            val allStockData = repository.fetchAllStockData()

            val stockData: List<BwibbuModel> = allStockData.component1().toList()
            val stockDayAvgData: List<StockDayAvgModel> = allStockData.component2().toList()
            val stockDayData: List<StockDayModel> = allStockData.component3().toList()

            val combinedData = stockData.mapNotNull { stock ->
                val dayData = stockDayData.find { it.code == stock.code }
                val avgData = stockDayAvgData.find { it.code == stock.code }
                if (dayData?.code != null && avgData?.code != null) {
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

            if (combinedData.isEmpty()) {
                _noDataAvailable.value = true
            } else {
                _noDataAvailable.value = false
                stockCardDao.insertStock(combinedData)
                _stockCardData.value = combinedData
            }
            _fetchingData.value = false
        }
    }

    fun fetchAllStockDataFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = stockCardDao.getAllStocks()
            if (data.isNotEmpty()) {
                _stockCardData.value = data
                _noDataAvailable.value = false
            } else {
                _noDataAvailable.value = true
            }
            _fetchingData.value = false
        }
    }

    fun sortStockData(ascending: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            var sortedData = _stockCardData.value
            try {
                if (ascending) {
                    _stockCardData.update {
                        sortedData.sortedBy { it.code.toInt() }
                    }
                } else {
                    _stockCardData.update {
                        sortedData.sortedByDescending { it.code.toInt() }
                    }
                }
            } finally {
                _fetchingData.value = false
            }
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