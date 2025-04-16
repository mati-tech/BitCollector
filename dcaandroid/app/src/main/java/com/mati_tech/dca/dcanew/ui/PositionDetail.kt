
package com.mati_tech.dca.dcanew.ui
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mati_tech.dca.databinding.ActivityPositionDetailBinding
import com.mati_tech.dca.dcanew.db.AppDatabase
import com.mati_tech.dca.dcanew.db.PositionDetailEntity
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.repos.PositionDetailRepos
import com.mati_tech.dca.dcanew.repos.PositionRepos
import com.mati_tech.dca.dcanew.utils.PositionDetailAdapter
import com.mati_tech.dca.dcanew.viewmodel.PositionDetailViewModel
import com.mati_tech.dca.dcanew.viewmodel.PositionDetailViewModelFactory
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModelFactory
import kotlinx.coroutines.launch

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
        viewModel.totalPnl.observe(this) { pnl ->
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
        binding.PositionDetailCloseButton.setOnClickListener{
            lifecycle.coroutineScope.launch {
                var position = positionViewModel.getPosition(positionId)
                showClosePositionDialog(position)
            }

        }

        // Fetch initial balances
        viewModel.fetchBalances(positionId)
    }

    private fun showClosePositionDialog(position: PositionEntity) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Close Position")
        builder.setMessage("Enter the closing price for ${position.coin}")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setView(input)

        // Set up buttons
        builder.setPositiveButton("Confirm") { _, _ ->
            val enteredPrice = input.text.toString().toDoubleOrNull()
            if (enteredPrice != null) {
//                val pnl = enteredPrice - position.buyPrice
                val pnl = (enteredPrice * position.amount) - (position.buyPrice * position.amount)
                // Format values to 2 decimal places
                val formattedPnl = String.format("%.4f", pnl).toDouble()
                val formattedEnteredPrice = String.format("%.4f", enteredPrice).toDouble()
                positionViewModel.closePosition(position.id,formattedPnl,  formattedEnteredPrice)
            } else {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
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
