
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
//                AlertDialog.Builder(this@PositionDetail)
//                    .setTitle("Delete Trade")
//                    .setMessage("Are you sure you want to delete this ${trade.tradeType} trade?")
//                    .setPositiveButton("Yes") { _, _ ->
//                        viewModel.deleteTrade(trade)
//                        Toast.makeText(
//                            this@PositionDetail,
//                            "Deleted ${trade.tradeType} trade",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    .setNegativeButton("No", null)
//                    .show()
                // Handle long-click (delete trade)
                viewModel.deleteTrade(trade)
                Toast.makeText(
                    this@PositionDetail,
                    "Deleted ${trade.tradeType} trade",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        binding.rvTradesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PositionDetail)
            adapter = positionDetailAdapter
        }

        // Observe price updates
        val coinNameUSDT = "${coinName}USDT"
        positionViewModel.cryptoPrices.observe(this, Observer { prices ->
            coinPrice = prices.find { it.symbol.contains(coinNameUSDT) }?.price?.toDouble() ?: 0.0
            binding.currentprice.text = "Current Price: $%.2f".format(coinPrice)
            viewModel.updatePriceAndCalculatePnl(coinPrice, positionId)

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
        viewModel.availableUsd.observe(this) { usd ->
            binding.tvAvailableUsdAmount.text = "Available USD: $%.2f".format(usd)
        }

        viewModel.currentHeldAmount.observe(this) { coins ->
            binding.tvCurrentCoinAmount.text = "Available Coins: %.8f".format(coins)
            binding.tvAvailableCoinAmount.text = "Available Coins: %.8f".format(coins)
        }

        viewModel.averageBuyPrice.observe(this) { price ->
            binding.tvAvergaeBuyPrice.text = "Avg Buy Price: $%.2f".format(price ?: 0.0)
        }
        viewModel.totalPNL.observe(this) { pnl ->
            binding.tvTotalPnl.text = "total PNL: %.2f$".format(pnl)
            if (pnl != null) {
                binding.tvTotalPnl.setTextColor(
                    if (pnl >= 0) Color.GREEN else Color.RED
                )
            }
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

    private fun updateCurrentPrice(coinPrice: Double) {

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


//package com.mati_tech.dca.dcanew.ui
//
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.mati_tech.dca.databinding.ActivityPositionDetailBinding
//import com.mati_tech.dca.dcanew.db.AppDatabase
//import com.mati_tech.dca.dcanew.db.PositionDetailEntity
//import com.mati_tech.dca.dcanew.repos.PositionDetailRepos
//import com.mati_tech.dca.dcanew.repos.PositionRepos
//import com.mati_tech.dca.dcanew.utils.PositionDetailAdapter
//import com.mati_tech.dca.dcanew.utils.roundTo4Decimals
//import com.mati_tech.dca.dcanew.viewmodel.PositionDetailViewModel
//import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel
//import com.mati_tech.dca.dcanew.viewmodel.PositionViewModelFactory
//
//class PositionDetail : AppCompatActivity() {
//
//    private lateinit var binding: ActivityPositionDetailBinding
//    private lateinit var positionDetailAdapter: PositionDetailAdapter
//    private lateinit var viewModel: PositionDetailViewModel
//    private lateinit var positionViewModel: PositionViewModel
//    private var coinPrice: Double = 0.0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityPositionDetailBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Extract intent data
//        val positionId = intent.getIntExtra("coin_id", -1)
//        val coinName = intent.getStringExtra("coin_name") ?: "Unknown"
//        val initialCoinAmount = intent.getStringExtra("total_amount")?.toDoubleOrNull()?.roundTo4Decimals()
//        val initialBuyPrice = intent.getStringExtra("avg_buy_price")?.toDoubleOrNull()?.roundTo4Decimals()
//
//        if (positionId == -1) {
//            Toast.makeText(this, "Invalid position ID", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//
//        // Initialize PositionViewModel
//        val positionRepos = PositionDetailRepos(AppDatabase.getDatabase(this).positionDao(), AppDatabase.getDatabase(this).positionDetailDao())
//        positionViewModel = PositionViewModelFactory(positionRepos)
//            .create(PositionViewModel::class.java)
//
//        // Initialize PositionDetailViewModel with SharedPreferences
//        val repos = PositionDetailRepos(AppDatabase.getDatabase(this).positionDetailDao())
//        val sharedPreferences = getSharedPreferences("dca_prefs", Context.MODE_PRIVATE)
//        viewModel = PositionDetailViewModel(repos, sharedPreferences)
//        viewModel.initialize(positionId)
//
//        // Setup RecyclerView
//        positionDetailAdapter = PositionDetailAdapter(object : PositionDetailAdapter.OnTradeClickListener {
//
//            override fun onTradeLongClick(trade: PositionDetailEntity) {
//                viewModel.deleteTrade(trade)
//                Toast.makeText(
//                    this@PositionDetail,
//                    "Deleted ${trade.tradeType} trade",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
//        binding.rvTradesRecyclerView.apply {
//            layoutManager = LinearLayoutManager(this@PositionDetail)
//            adapter = positionDetailAdapter
//        }
//
//        // Observe price updates
//        val coinNameUSDT = "${coinName}USDT"
//        positionViewModel.cryptoPrices.observe(this, Observer { prices ->
//            coinPrice = prices.find { it.symbol.contains(coinNameUSDT) }?.price?.toDouble()?.roundTo4Decimals() ?: 0.0
//            binding.currentprice.text = "Current Price: $%.4f".format(coinPrice)
//        })
//
//        // Observe trades and initialize
//        viewModel.getTradesByPositionId(positionId).observe(this) { trades ->
//            Log.d("PositionDetail", "Trades for positionId $positionId: $trades")
//            positionDetailAdapter.submitList(trades)
//            if (trades.isEmpty() && initialCoinAmount != null && initialBuyPrice != null) {
//                val totalCost = (initialCoinAmount * initialBuyPrice).roundTo4Decimals()
//                val initialTrade = PositionDetailEntity(
//                    positionId = positionId,
//                    tradeType = "BUY",
//                    tradeAmount = initialCoinAmount,
//                    tradePrice = initialBuyPrice,
//                    totalCost = totalCost
//                )
//                viewModel.insertTrade(initialTrade)
//            }
//            binding.tvCoinName.text = coinName
//            binding.tvInitialCoinAmount.text = "Initial Amount: %.4f".format(initialCoinAmount ?: 0.0)
//            binding.tvInitialBuyPrice.text = "Initial Buy Price: $%.4f".format(initialBuyPrice ?: 0.0)
//        })
//
//        // Observe UI data
//        viewModel.netUsdBalance.observe(this) { usd ->
//            Log.d("PositionDetail", "netUsdBalance updated: $usd")
//            binding.tvAvailableUsdAmount.text = "Available USD: $%.4f".format(usd)
//        }
//
//        viewModel.currentHeldAmount.observe(this) { coins ->
//            binding.tvCurrentCoinAmount.text = "Available Coins: %.4f".format(coins)
//            binding.tvAvailableCoinAmount.text = "Available Coins: %.4f".format(coins)
//        }
//
//        viewModel.averageBuyPrice.observe(this) { price ->
//            binding.tvAvergaeBuyPrice.text = "Avg Buy Price: $%.4f".format(price ?: 0.0)
//        }
//
//        viewModel.errorLiveData.observe(this) { error ->
//            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
//        }
//
//        // Button listeners
//        binding.btnBuy.setOnClickListener {
//            if (coinPrice > 0.0) {
//                viewModel.buyAllAvailable(positionId, coinPrice)
//            } else {
//                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.btnSell.setOnClickListener {
//            if (coinPrice > 0.0) {
//                viewModel.sellAllAvailable(positionId, coinPrice)
//            } else {
//                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // Fetch balances
//        viewModel.fetchBalances(positionId)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val coinName = intent.getStringExtra("coin_name") ?: return
//        val coinNameUSDT = "${coinName}USDT"
//        positionViewModel.startFetchingPrices(listOf(coinNameUSDT))
//    }
//
//    override fun onPause() {
//        super.onPause()
//        positionViewModel.stopFetchingPrice()
//    }
//}
//---------------------
