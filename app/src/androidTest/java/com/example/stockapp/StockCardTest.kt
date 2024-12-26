package com.example.stockapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.stockapp.data.dao.StockCardDao
import com.example.stockapp.data.model.StockCardModel
import com.example.stockapp.data.repository.StockRepository
import com.example.stockapp.network.StockApi
import com.example.stockapp.ui.component.StockCard
import com.example.stockapp.viewmodel.StockViewModel
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class StockCardTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var stockApi: StockApi
    private lateinit var stockCardDao: StockCardDao
    private lateinit var stockRepository: StockRepository
    private lateinit var stockViewModel: StockViewModel

    @Before
    fun setup() {
        stockApi = mockk()
        stockCardDao = mockk()
        stockRepository = StockRepository(stockApi)
        stockViewModel = StockViewModel(stockRepository, stockCardDao)
    }

    @Test
    fun testStockCardDisplaysCorrectInformation() {
        val stockCardModel = StockCardModel(
            code = "1101",
            name = "台泥",
            openingPrice = "32.25",
            closingPrice = "32.25",
            highestPrice = "32.50",
            lowestPrice = "32.20",
            change = "0.0000",
            transaction = "2852",
            tradeVolume = "5048211",
            tradeValue = "163264837",
            peRatio = "27.10",
            dividendYield = "3.10",
            pbRatio = "1.01",
            monthlyAveragePrice = "600"
        )

        composeTestRule.setContent {
            MaterialTheme {
                StockCard(stockViewModel, stockCardModel)
            }
        }

        composeTestRule.onNodeWithText("1101 台泥").assertExists()
        composeTestRule.onNodeWithText("開盤價").assertExists()
        composeTestRule.onNodeWithText("32.25").assertExists()
        composeTestRule.onNodeWithText("收盤價").assertExists()
        composeTestRule.onNodeWithText("32.25").assertExists()
        composeTestRule.onNodeWithText("最高價").assertExists()
        composeTestRule.onNodeWithText("32.50").assertExists()
        composeTestRule.onNodeWithText("最低價").assertExists()
        composeTestRule.onNodeWithText("32.20").assertExists()
        composeTestRule.onNodeWithText("漲跌價差").assertExists()
        composeTestRule.onNodeWithText("0.0000").assertExists()
        composeTestRule.onNodeWithText("月平均價").assertExists()
        composeTestRule.onNodeWithText("600").assertExists()
        composeTestRule.onNodeWithText("成交筆數").assertExists()
        composeTestRule.onNodeWithText("2852").assertExists()
        composeTestRule.onNodeWithText("成交股數").assertExists()
        composeTestRule.onNodeWithText("5048211").assertExists()
        composeTestRule.onNodeWithText("成交金額").assertExists()
        composeTestRule.onNodeWithText("163264837").assertExists()
    }

    @Test
    fun testStockCardClickAction() {
        val stockCardModel = StockCardModel(
            code = "1101",
            name = "台泥",
            openingPrice = "32.25",
            closingPrice = "32.25",
            highestPrice = "32.50",
            lowestPrice = "32.20",
            change = "0.0000",
            transaction = "2852",
            tradeVolume = "5048211",
            tradeValue = "163264837",
            peRatio = "27.10",
            dividendYield = "3.10",
            pbRatio = "1.01",
            monthlyAveragePrice = "600"
        )

        composeTestRule.setContent {
            MaterialTheme {
                StockCard(stockViewModel, stockCardModel)
            }
        }

        composeTestRule.onNodeWithText("1101 台泥").performClick()
        assert(stockViewModel.showStockRatioDialog.value)
    }

}