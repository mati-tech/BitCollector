//package com.mati_tech.dca.dcanew.ui
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.mati_tech.dca.R
//import com.mati_tech.dca.databinding.ActivityPositionDetailBinding
//import com.mati_tech.dca.dcanew.db.AppDatabase
//import com.mati_tech.dca.dcanew.db.PositionDetailEntity
//import com.mati_tech.dca.dcanew.remote.CryptoResponse
//import com.mati_tech.dca.dcanew.repos.PositionDetailRepos
//import com.mati_tech.dca.dcanew.repos.PositionRepos
//import com.mati_tech.dca.dcanew.utils.PositionDetailAdapter
//import com.mati_tech.dca.dcanew.viewmodel.PositionDetailViewModel
//import com.mati_tech.dca.dcanew.viewmodel.PositionDetailViewModelFactory
//import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel
//import com.mati_tech.dca.dcanew.viewmodel.PositionViewModelFactory
//
//class PositionDetail : AppCompatActivity() {
//
//    private lateinit var binding: ActivityPositionDetailBinding
//    private lateinit var positiondetailAdapter: PositionDetailAdapter
//    private lateinit var repos: PositionDetailRepos
//    private lateinit var positionrepos: PositionRepos
//
//    private lateinit var viewmodel: PositionDetailViewModel
//    private lateinit var positionviewmodel: PositionViewModel
//
//    private val pricesList = mutableListOf<CryptoResponse>()
//    private  var coinPrice: Double = 0.0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityPositionDetailBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val positionId = intent.getIntExtra("coin_id", -1)
//        val coinName = intent.getStringExtra("coin_name")
//        val coinNameUSDT = coinName+"USDT"
//        val initialCoinAmount = intent.getStringExtra("total_amount")
//        val initialBuyPrice = intent.getStringExtra("avg_buy_price")
//
//        val positiondb = AppDatabase.getDatabase(this)
//        positionrepos = PositionRepos(positiondb.positionDao())
//        val Positionfactory = PositionViewModelFactory(positionrepos)
//        positionviewmodel = ViewModelProvider(this, Positionfactory).get(PositionViewModel::class.java)
//
//        positionviewmodel.fetchPositions()
//        positionviewmodel.cryptoPrices.observe(this, Observer { prices ->
//
//            if (prices.isNotEmpty()){
//                pricesList.clear()
//                pricesList.addAll(prices)
//                println(pricesList)
//                coinPrice = pricesList.find { it.symbol.contains(coinNameUSDT) }?.price?.toDouble() ?: 0.0
//                binding.currentprice.text = "Current Price: $coinPrice"
////                adapter.updateCurrentPrices(btcPrice, ethPrice, ltcPrice, adaPrice, dotPrice, avaxPrice, arbPrice, xrpPrice, solPrice, aptPrice)
////                updatepnl()
////                updateCurrentValue()
//            }
//            else {
//                // Handle no data or error scenario
//                binding.currentprice.text =  "Network Error!"
//            }
//        })
//
//
//        val db = AppDatabase.getDatabase(this)
//        repos = PositionDetailRepos(db.positionDetailDao())
//
//        val factory = PositionDetailViewModelFactory(repos)
//        viewmodel = ViewModelProvider(this, factory).get(PositionDetailViewModel::class.java)
//
//        val recyclerView = binding.rvTradesRecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        positiondetailAdapter = PositionDetailAdapter(viewmodel)
//        recyclerView.adapter = positiondetailAdapter
//
//
//        viewmodel.getTradesByPositionId(positionId).observe(this) { positions ->
//            Log.d("PositionDetail", "Observing trades for positionId: $positionId. Position: $positions")
//            if (positions.isNullOrEmpty()) {
//                Log.d("PositionDetail", "Position list is empty, inserting new data.")
//                // Insert it since it's the first time
//                val amount = initialCoinAmount?.toDoubleOrNull()
//                val price = initialBuyPrice?.toDoubleOrNull()
//                if (amount != null && price != null) {
//                    val totalCost = amount * price
//                    val newPosition = PositionDetailEntity(
//                        positionId = positionId,
//                        tradeType = "BUY",
//                        tradeAmount = amount,
//                        tradePrice = price,
//                        totalCost = totalCost
//                    )
//
//                    Log.d("PositionDetail", "Inserting new trade: $newPosition")
//                    // Insert new data
//                    viewmodel.insertTrade(newPosition)
//
//                    // Fetch updated data after insert
//                    viewmodel.getTradesByPositionId(positionId).observe(this) { updatedPosition ->
//                        Log.d("PositionDetail", "Updated trades fetched: $updatedPosition")
//                        positiondetailAdapter.updateData(updatedPosition)
//                    }
//
//                    // Update UI
//                    binding.tvCoinName.text = coinName
//                    binding.tvInitialCoinAmount.text = "Initial Amount: $initialCoinAmount"
//                    binding.tvInitialBuyPrice.text = "Initial Buy Price: $$initialBuyPrice"
//                } else {
//                    Log.d("PositionDetail", "Invalid data for amount or price.")
//                    Toast.makeText(this, "Invalid intent data", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Log.d("PositionDetail", "Position list is not empty, updating adapter with existing data.")
//                // Handle existing data
//                positiondetailAdapter.updateData(positions)
//
//                // Set TextViews from DB
//                binding.tvCoinName.text = coinName
//                binding.tvInitialCoinAmount.text = "Initial Amount: $initialCoinAmount"
//                binding.tvInitialBuyPrice.text = "Initial Buy Price: $$initialBuyPrice"
//            }
//        }
//
//        viewmodel.getCurrentHeldAmount(positionId){coinheld ->
//            runOnUiThread {
//                binding.tvCurrentCoinAmount.text = "Available: %.2f".format(coinheld)
//                binding.tvAvailableCoinAmount.text = "Available: %.2f".format(coinheld)
//            }
//        }
////        viewmodel.getTotalBoughtAmount(positionId) {totalBought ->
//
////        }
//        viewmodel.getAverageBuyPrice(positionId) { avgPrice ->
//            runOnUiThread {
//                binding.tvAvergaeBuyPrice.text = "Avg Price: $%.2f".format(avgPrice)
//            }
//        }
//        viewmodel.getTotalSellValue(positionId) { totalusd ->
//            runOnUiThread {
//                binding.tvAvailableUsdAmount.text = "Available Usd: $%.2f".format(totalusd)
//            }
//        }
//
//// Observe any error messages
//        viewmodel.errorLiveData.observe(this, Observer { errorMessage ->
//            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//        })
//
//        // Buy button functionality
////        binding.btnBuy.setOnClickListener {
////            val positionId = intent.getIntExtra("coin_id", -1)
////            val availableUsd = binding.tvAvailableUsdAmount.text.toString().replace("Available Usd: $", "").toDoubleOrNull() ?: 0.0
////            if (availableUsd > 0.0 && coinPrice > 0.0) {
////                val amountToBuy = availableUsd / coinPrice
////                viewmodel.buy(positionId, amountToBuy, coinPrice)
////                updateTextViews(positionId)
////            } else {
////                Toast.makeText(this, "Invalid USD amount or price", Toast.LENGTH_SHORT).show()
////            }
////        }
//
//        binding.btnBuy.setOnClickListener {
//            val positionId = intent.getIntExtra("coin_id", -1)
//            viewmodel.getTotalSellValue(positionId) { availableUsd ->
//                if (availableUsd > 0.0 && coinPrice > 0.0) {
//                    // Round to avoid precision issues (e.g., 8 decimals for most coins)
//                    val amountToBuy = (availableUsd / coinPrice * 100000000.0).toLong() / 100000000.0
//                    viewmodel.buy(positionId, amountToBuy, coinPrice)
//                    updateTextViews(positionId)
//                } else {
//                    Toast.makeText(this, "No USD available or invalid price", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//        // Sell button functionality
//        binding.btnSell.setOnClickListener {
//            val positionId = intent.getIntExtra("coin_id", -1)
//            viewmodel.getCurrentHeldAmount(positionId) { availableCoin ->
//                if (availableCoin > 0.0 && coinPrice > 0.0) {
//                    // Round to avoid precision issues
//                    val roundedCoin = (availableCoin * 100000000.0).toLong() / 100000000.0
//                    val totalSellValue = roundedCoin * coinPrice
//                    viewmodel.sell(positionId, roundedCoin, totalSellValue)
//                    updateTextViews(positionId)
//                } else {
//                    Toast.makeText(this, "No coins available or invalid price", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//
//    }
//
//
//    private fun updateTextViews(positionId: Int) {
//        // Re-fetch the latest values from ViewModel and update UI
//        viewmodel.getCurrentHeldAmount(positionId) { coinheld ->
//            runOnUiThread {
//                binding.tvCurrentCoinAmount.text = "Available: %.2f".format(coinheld)
//                binding.tvAvailableCoinAmount.text = "Available: %.2f".format(coinheld)
//            }
//        }
//
//        viewmodel.getAverageBuyPrice(positionId) { avgPrice ->
//            runOnUiThread {
//                binding.tvAvergaeBuyPrice.text = "Avg Price: $%.2f".format(avgPrice)
//            }
//        }
//
//        viewmodel.getTotalSellValue(positionId) { totalusd ->
//            runOnUiThread {
//                binding.tvAvailableUsdAmount.text = "Available Usd: $%.2f".format(totalusd)
//            }
//        }
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        val coinName = intent.getStringExtra("coin_name")
//        val coinNameUSDT = coinName+"USDT"
//        val cryptoList = listOf(coinNameUSDT)
//        positionviewmodel.startFetchingPrices(cryptoList) // Starts fetching price every second
//
//
//    }
//    override fun onPause() {
//        super.onPause()
//        positionviewmodel.stopFetchingPrice() // Stops fetching when app is in background
//    }
//
//
//}

package com.mati_tech.dca.dcanew.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mati_tech.dca.databinding.ActivityPositionDetailBinding
import com.mati_tech.dca.dcanew.db.AppDatabase
import com.mati_tech.dca.dcanew.db.PositionDetailEntity
import com.mati_tech.dca.dcanew.repos.PositionDetailRepos
import com.mati_tech.dca.dcanew.repos.PositionRepos
import com.mati_tech.dca.dcanew.utils.PositionDetailAdapter
import com.mati_tech.dca.dcanew.viewmodel.PositionDetailViewModel
import com.mati_tech.dca.dcanew.viewmodel.PositionDetailViewModelFactory
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModelFactory

class PositionDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPositionDetailBinding
    private lateinit var positionDetailAdapter: PositionDetailAdapter
    private lateinit var viewModel: PositionDetailViewModel
    private lateinit var positionViewModel: PositionViewModel
    private var coinPrice: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPositionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Extract intent data
        val positionId = intent.getIntExtra("coin_id", -1)
        val coinName = intent.getStringExtra("coin_name") ?: "Unknown"
        val initialCoinAmount = intent.getStringExtra("total_amount")?.toDoubleOrNull()
        val initialBuyPrice = intent.getStringExtra("avg_buy_price")?.toDoubleOrNull()

        if (positionId == -1) {
            Toast.makeText(this, "Invalid position ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize PositionViewModel for price updates
        val positionRepos = PositionRepos(AppDatabase.getDatabase(this).positionDao())
        positionViewModel = PositionViewModelFactory(positionRepos)
            .create(PositionViewModel::class.java)

        // Initialize PositionDetailViewModel
        val repos = PositionDetailRepos(AppDatabase.getDatabase(this).positionDetailDao())
        viewModel = PositionDetailViewModelFactory(repos)
            .create(PositionDetailViewModel::class.java)

        // Setup RecyclerView
        positionDetailAdapter = PositionDetailAdapter(object : PositionDetailAdapter.OnTradeClickListener {

            override fun onTradeLongClick(trade: PositionDetailEntity) {
                AlertDialog.Builder(this@PositionDetail)
                    .setTitle("Delete Trade")
                    .setMessage("Are you sure you want to delete this ${trade.tradeType} trade?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteTrade(trade)
                        Toast.makeText(
                            this@PositionDetail,
                            "Deleted ${trade.tradeType} trade",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("No", null)
                    .show()
                // Handle long-click (delete trade)
//                viewModel.deleteTrade(trade)
//                Toast.makeText(
//                    this@PositionDetail,
//                    "Deleted ${trade.tradeType} trade",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        })
//        positionDetailAdapter = PositionDetailAdapter()
        binding.rvTradesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PositionDetail)
            adapter = positionDetailAdapter
        }

        // Observe price updates
        val coinNameUSDT = "${coinName}USDT"
        positionViewModel.cryptoPrices.observe(this, Observer { prices ->
            coinPrice = prices.find { it.symbol.contains(coinNameUSDT) }?.price?.toDouble() ?: 0.0
            binding.currentprice.text = "Current Price: $%.2f".format(coinPrice)
        })

        // Observe trades and initialize if empty
        viewModel.getTradesByPositionId(positionId).observe(this) { trades ->
            Log.d("PositionDetail", "Trades for positionId $positionId: $trades")
            positionDetailAdapter.submitList(trades)
            if (trades.isEmpty() && initialCoinAmount != null && initialBuyPrice != null) {
                // Insert initial trade only once
                val totalCost = initialCoinAmount * initialBuyPrice
                val initialTrade = PositionDetailEntity(
                    positionId = positionId,
                    tradeType = "BUY",
                    tradeAmount = initialCoinAmount,
                    tradePrice = initialBuyPrice,
                    totalCost = totalCost
                )
                viewModel.insertTrade(initialTrade)
            }
            // Update UI with static data
            binding.tvCoinName.text = coinName
            binding.tvInitialCoinAmount.text = "Initial Amount: %.8f".format(initialCoinAmount ?: 0.0)
            binding.tvInitialBuyPrice.text = "Initial Buy Price: $%.2f".format(initialBuyPrice ?: 0.0)
        }

        // Observe dynamic UI data
        viewModel.netUsdBalance.observe(this) { usd ->
            binding.tvAvailableUsdAmount.text = "Available USD: $%.2f".format(usd)
        }

        viewModel.currentHeldAmount.observe(this) { coins ->
            binding.tvCurrentCoinAmount.text = "Available Coins: %.8f".format(coins)
            binding.tvAvailableCoinAmount.text = "Available Coins: %.8f".format(coins)
        }

        viewModel.averageBuyPrice.observe(this) { price ->
            binding.tvAvergaeBuyPrice.text = "Avg Buy Price: $%.2f".format(price ?: 0.0)
        }

        // Observe errors
        viewModel.errorLiveData.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        // Button listeners
        binding.btnBuy.setOnClickListener {
            if (coinPrice > 0.0) {
                binding.tvTradeStatusName.text = "Status: Hold"
                binding.tvTradeStatusName.setTextColor(Color.GREEN)
                viewModel.buyAllAvailable(positionId, coinPrice)
            } else {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSell.setOnClickListener {
            if (coinPrice > 0.0) {
                viewModel.sellAllAvailable(positionId, coinPrice)
                binding.tvTradeStatusName.text = "Status: Sold"
                binding.tvTradeStatusName.setTextColor(Color.RED)
            } else {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch initial balances
        viewModel.fetchBalances(positionId)
    }

    override fun onResume() {
        super.onResume()
        val coinName = intent.getStringExtra("coin_name") ?: return
        val coinNameUSDT = "${coinName}USDT"
        positionViewModel.startFetchingPrices(listOf(coinNameUSDT))
    }

    override fun onPause() {
        super.onPause()
        positionViewModel.stopFetchingPrice()
    }
}