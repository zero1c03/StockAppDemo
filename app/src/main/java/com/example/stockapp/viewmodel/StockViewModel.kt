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

/**
 * ViewModel for managing stock data.
 *
 * @property repository The repository for fetching stock data.
 * @property stockCardDao The DAO for accessing stock card data.
 */
@HiltViewModel
class StockViewModel @Inject constructor(
    private val repository: StockRepository,
    private val stockCardDao: StockCardDao
) : ViewModel() {

    // State to track if the data has been initialized.
    private val _initialized = mutableStateOf(false)
    val initialized: State<Boolean> = _initialized

    // State to track if the sort bottom sheet is shown.
    private val _showSortBottomSheet = mutableStateOf(false)
    val showSortBottomSheet: State<Boolean> = _showSortBottomSheet

    // State to track if the stock ratio dialog is shown.
    private val _showStockRatioDialog = mutableStateOf(false)
    val showStockRatioDialog: State<Boolean> = _showStockRatioDialog

    // State to track if data is being fetched.
    private val _fetchingData = mutableStateOf(false)
    val fetchingData: State<Boolean> = _fetchingData

    // State to track if no data is available.
    private val _noDataAvailable = mutableStateOf(true)
    val noDataAvailable: State<Boolean> = _noDataAvailable

    // StateFlow to hold the stock card data.
    private val _stockCardData = MutableStateFlow<List<StockCardModel>>(emptyList())
    val stockCardData: StateFlow<List<StockCardModel>> = _stockCardData.asStateFlow()

    // StateFlow to hold the data for the stock ratio dialog.
    private val _showStockRatioDialogData = MutableStateFlow<StockCardModel>(StockCardModel())
    val showStockRatioDialogData: StateFlow<StockCardModel> = _showStockRatioDialogData

    /**
     * Sets the initialized state.
     *
     * @param value The new value for the initialized state.
     */
    fun setInitialized(value: Boolean) {
        _initialized.value = value
    }

    /**
     * Fetches all stock data based on network availability.
     *
     * @param isNetworkAvailable True if the network is available, false otherwise.
     */
    fun showAllStockData(isNetworkAvailable: Boolean) {
        _fetchingData.value = true
        if (isNetworkAvailable) {
            fetchAllStockData()
        } else {
            fetchAllStockDataFromDatabase()
        }
    }

    /**
     * Fetches all stock data from the repository.
     */
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

    /**
     * Fetches all stock data from the database.
     */
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

    /**
     * Combines stock data from different sources into a list of StockCardModel.
     *
     * @param allStockData A Triple containing lists of BwibbuModel, StockDayAvgModel, and StockDayModel.
     * @return A list of StockCardModel.
     */
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

    /**
     * Updates the stock data in the database and state.
     *
     * @param data The new stock data to be updated.
     */
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

    /**
     * Sorts the stock data in ascending or descending order.
     *
     * @param ascending True to sort in ascending order, false to sort in descending order.
     */
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

    /**
     * Shows or hides the sort bottom sheet.
     *
     * @param show True to show the sort bottom sheet, false to hide it.
     */
    fun showSortBottomSheet(show: Boolean) {
        _showSortBottomSheet.value = show
    }

    /**
     * Shows or hides the stock ratio dialog.
     *
     * @param show True to show the stock ratio dialog, false to hide it.
     * @param showStockRatioDialogData The data to be displayed in the stock ratio dialog.
     */
    fun showStockRatioDialog(show: Boolean, showStockRatioDialogData: StockCardModel) {
        _showStockRatioDialog.value = show
        _showStockRatioDialogData.value = showStockRatioDialogData
    }
}