package com.mati_tech.dca.dcanew.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mati_tech.dca.databinding.ActivityDcacalculatorBinding
import com.mati_tech.dca.dcanew.db.AppDatabase
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.remote.CryptoResponse

import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel

import com.mati_tech.dca.dcanew.repos.PositionRepos
import com.mati_tech.dca.dcanew.utils.PositionAdapter
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModelFactory
import kotlinx.coroutines.async

/*
steps for adding a new coin to the code:
first: add the variable with coin name
second: use the var to get the price from fetched pricelist in the viewmodel observer
third: add it in the text view and in the function in the viewmodel to fetch the price from the list
fourth: update the updatecurrentPrice function in the viewmodel
fifth: in the onResumemethod in the startFetchingPrice make put the coin name also
sixth: in the spinner also add the coin name
 */
/*
bnb, trx, link, ton, xlm, sui, near, ondo , fil, ena, cyber,
 */


class DcaCalculator : AppCompatActivity() {
    private lateinit var viewModel: PositionViewModel
    private lateinit var adapter: PositionAdapter
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityDcacalculatorBinding
    private lateinit var repos: PositionRepos


//
    private val pricesList = mutableListOf<CryptoResponse>()
    private var btcPrice : Double = 0.0
    private var ethPrice : Double = 0.0
    private var ltcPrice : Double = 0.0
    private var adaPrice : Double = 0.0
    private var dotPrice : Double = 0.0
    private var avaxPrice : Double = 0.0
    private var arbPrice : Double = 0.0
    private var solPrice : Double = 0.0
    private var xrpPrice : Double = 0.0
    private var aptPrice : Double = 0.0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDcacalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = AppDatabase.getDatabase(this)
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        var balance = sharedPreferences.getFloat("balance", 0.0f)
        val formattedBalance = String.format("%.2f", balance)
        binding.TotalMoneyInCrypto.text = "Total Balance: $formattedBalance$"

        repos = PositionRepos(db.positionDao())
        val factory = PositionViewModelFactory(repos)
        viewModel = ViewModelProvider(this, factory).get(PositionViewModel::class.java)

        viewModel.fetchPositions()
        viewModel.cryptoPrices.observe(this, Observer { prices ->

            if (prices.isNotEmpty()){
                pricesList.clear()
                pricesList.addAll(prices)
                println(pricesList)

                btcPrice = pricesList.find { it.symbol.contains("BTCUSDT") }?.price?.toDouble() ?: 0.0
                ethPrice = pricesList.find { it.symbol.contains("ETHUSDT") }?.price?.toDouble() ?: 0.0
                ltcPrice = pricesList.find { it.symbol.contains("LTCUSDT") }?.price?.toDouble() ?: 0.0
                adaPrice = pricesList.find { it.symbol.contains("ADAUSDT") }?.price?.toDouble() ?: 0.0
                dotPrice = pricesList.find { it.symbol.contains("DOTUSDT") }?.price?.toDouble() ?: 0.0
                avaxPrice = pricesList.find { it.symbol.contains("AVAXUSDT")}?.price?.toDouble() ?: 0.0
                xrpPrice = pricesList.find { it.symbol.contains("XRPUSDT")}?.price?.toDouble() ?: 0.0
                arbPrice = pricesList.find { it.symbol.contains("ARBUSDT") }?.price?.toDouble() ?: 0.0
                solPrice = pricesList.find { it.symbol.contains("SOLUSDT") }?.price?.toDouble() ?: 0.0
                aptPrice = pricesList.find { it.symbol.contains("APTUSDT")  }?.price?.toDouble() ?: 0.0


                binding.currentprice.text =
                    "BTC: $btcPrice$ ---- LTC: $ltcPrice$\n"+
                            "ETH: $ethPrice$ ---- ADA: $adaPrice$\n" +
                            "DOT: $dotPrice$ ---- AVAX: $avaxPrice$\n" +
                            "XRP: $xrpPrice$ ---- ARB: $arbPrice$\n" +
                            "SOL: $solPrice$ ---- APT: $aptPrice$\n"

                adapter.updateCurrentPrices(btcPrice, ethPrice, ltcPrice, adaPrice, dotPrice, avaxPrice, arbPrice, xrpPrice, solPrice, aptPrice)

                updatepnl()
                updateCurrentValue()
            }
            else {
                // Handle no data or error scenario
                binding.currentprice.text =  "Network Error!"
            }
        })


//        val recyclerView = binding.
//        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PositionAdapter(viewModel,
            onCloseatClick = { position ->
//            viewModel.closePosition(position.id, repos.getCurrentPriceFromApi(position.coin))
                showClosePositionDialog(position)
            },
            onDeleteClick = { position ->
                showDeletePositionConfirmationBox(position)
//                val currentPrice = adapter.getCurrentPrice(position.coin) // Get latest price
//                viewModel.closePosition(position.id, currentPrice) // Pass updated price
            },
            onItemClick = {
                position ->

            }
            )

//
        // Observe total sum
        viewModel.totalInvested.observe(this) { total ->
            val balance = sharedPreferences.getFloat("balance", 0.0f)
            binding.TotalMoneyUsd.text = "In Positions: ${String.format("%.2f", total)}$"
            val availableMoney = balance - total
            binding.TotalFreeMoney.text = "Available balance:\n ${String.format("%.2f", availableMoney)}$"
        }
        // Fetch total initially and refresh when needed
        viewModel.fetchTotalInvested()

        setupSpinner()
        setupInputListeners()
        updateCurrentValue()
        binding.btnAddPosition.setOnClickListener {
            addPosition()
        }

        binding.historyButton.setOnClickListener{
            val intent = Intent(this, PositionHistory::class.java)

            startActivity(intent)
        }
        binding.btnCurrentPositions.setOnClickListener {
            val intent = Intent(this, OngoingPositions::class.java)
            startActivity(intent)
        }
        binding.profileIcon.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }
    }

    private fun showDeletePositionConfirmationBox(position: PositionEntity) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Close Position")
        builder.setMessage("Are you sure of closing position for: ${position.coin}")

        // Set up buttons
        builder.setPositiveButton("Confirm") { _, _ ->
            val currentPrice = adapter.getCurrentPrice(position.coin) // Get latest price
//            val pnl = currentPrice - position.buyPrice
            val pnl = (currentPrice * position.amount) - (position.buyPrice * position.amount)
            // Format values to 2 decimal places
            val formattedPnl = String.format("%.2f", pnl).toDouble()
            val formattedCurrentPrice = String.format("%.2f", currentPrice).toDouble()
//            viewModel.closePosition(position.id, pnl, currentPrice)
            viewModel.closePosition(position.id, formattedPnl, formattedCurrentPrice)
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun updatepnl() {
        viewModel.positions.observe(this, Observer{positions ->

            val totalPnl = adapter.findTotalPnl(positions )
            binding.TotalProfitLoss.text = "Total PnL : ${String.format("%.2f", totalPnl)}$"
            // Change background color based on the value of totalPnl
            if (totalPnl < 0) {
                binding.TotalProfitLoss.setBackgroundColor(Color.RED) // Red for negative PnL
            } else {
                binding.TotalProfitLoss.setBackgroundColor(Color.GREEN) // Reset for non-negative PnL
            }
        })
    }


    private fun showClosePositionDialog(position: PositionEntity) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Close Position")
        builder.setMessage("Enter the closing price for ${position.coin}")

        // Create an EditText for user input
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
                val formattedPnl = String.format("%.2f", pnl).toDouble()
                val formattedEnteredPrice = String.format("%.2f", enteredPrice).toDouble()
                viewModel.closePosition(position.id,formattedPnl,  formattedEnteredPrice)
            } else {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun setupSpinner() {
        val coins = listOf("BTC", "LTC", "SOL", "ETH", "XRP", "ADA", "DOT", "AVAX", "ARB", "APT")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, coins)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.coinNameSpinner.adapter = adapter

        // Set "LTC" as the default selected item
        val defaultPosition = coins.indexOf("LTC")
        if (defaultPosition != -1) {
            binding.coinNameSpinner.setSelection(defaultPosition)
        }
//        binding.coinNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedCoin = coins[position]
//                binding.CoinName.setText(selectedCoin)  // Set coin name in EditText
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
////                binding.editName.setText("")  // Clear EditText if nothing is selected
//            }
//        }
    }

    private fun setupInputListeners() {

        binding.ValueinUSD.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateamountCoin()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.BuyPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateamountCoin()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun calculateamountCoin() {
        val buyPrice = binding.BuyPrice.text.toString().toDoubleOrNull() ?: 0.0
        val ValueUsd = binding.ValueinUSD.text.toString().toDoubleOrNull() ?: 0.0
        if (buyPrice > 0 && ValueUsd > 0) {
            binding.AmountinCoin.setText(String.format("%.2f", ValueUsd / buyPrice))
        } else {
            binding.AmountinCoin.text.clear()
        }
    }

    private fun addPosition() {
        val coin = binding.coinNameSpinner.selectedItem.toString()
        val buyPrice = binding.BuyPrice.text.toString().toDoubleOrNull() ?: return
        val amount = binding.AmountinCoin.text.toString().toDoubleOrNull() ?: return
        val valueinusd = binding.ValueinUSD.text.toString().toDoubleOrNull() ?: return
        val position =
            PositionEntity(coin = coin, buyPrice = buyPrice, amount = amount, valueUsd = valueinusd)
        viewModel.addPosition(position)
        viewModel.fetchTotalInvested()
        binding.BuyPrice.text.clear()
        binding.AmountinCoin.text.clear()
        binding.ValueinUSD.text.clear()

    }
    override fun onResume() {
        super.onResume()
        val cryptoList = listOf("BTCUSDT", "ETHUSDT", "LTCUSDT", "ADAUSDT", "XRPUSDT", "DOTUSDT", "SOLUSDT", "AVAXUSDT", "ARBUSDT", "APTUSDT")
        viewModel.startFetchingPrices(cryptoList) // Starts fetching price every second

        viewModel.fetchTotalInvested()
        var balance = sharedPreferences.getFloat("balance", 0.0f)
        val formattedBalance = String.format("%.2f", balance)
        binding.TotalMoneyInCrypto.text = "Total Balance: $formattedBalance$"
        updateCurrentValue()

    }

    override fun onPause() {
        super.onPause()
        viewModel.stopFetchingPrice() // Stops fetching when app is in background
    }

    fun GoToOnGoingPositions(view: View) {
        binding.btnCurrentPositions.setOnClickListener {
            val intent = Intent(it.context, OngoingPositions::class.java)
            it.context.startActivity(intent)
            true // Return true to indicate the long press was handled
        }

    }

    fun GoToProfile(view: View) {
        binding.profileIcon.setOnClickListener {
        val intent = Intent(it.context, UserProfile::class.java)
        it.context.startActivity(intent)
        true // Return true to indicate the long press was handled
    }}
    private fun updateCurrentValue() {

        viewModel.totalInvested.observe(this) { totalInvested ->
            viewModel.positions.observe(this) { positions ->
                val totalPnl = adapter.findTotalPnl(positions)  // Get total PnL
                val currentValue = totalInvested + totalPnl  // Calculate Current Value

// Log values to check correctness
                Log.d("INVEST_DEBUG", "Total Invested: $totalInvested")
                Log.d("PNL_DEBUG", "Total PnL: $totalPnl")
                Log.d("CURRENT_VALUE_DEBUG", "Calculated Current Value: $currentValue")
                // Update UI
                binding.TotalCurrentMoneyUsd.text = "Current Balance\nif close all now: ${String.format("%.2f", currentValue)}$"
            }
        }
    }

}

//    private fun calculateTotalProfitLoss(): Double {
//
////        var totalProfitLoss = adapter.findTotalPnl(viewModel.positions)
////        tvTotalProfitLoss.setText("P&L: ${String.format("%.2f", tvTotalProfitLoss)}$ ")
//        binding.TotalProfitLoss.text = "P&L: ${String.format("%.2f", totalProfitLoss)}$ "
//        // For now, we just return 0
////        return totalProfitLoss



//    private fun startUpdatingPrice() {
//        val runnable = object : Runnable {
//            override fun run() {
//                fetchCoinPrice()
//                calculateTotalProfitLoss()
//                handler.postDelayed(this, 1000) // Repeat every second
//            }
//        }
//        handler.post(runnable)
//    }



//    private fun fetchCoinPrice() {
//        api.getCoinPrice("LTCUSDT").enqueue(object : Callback<Map<String, String>> {
//            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
//                if (response.isSuccessful) {
//                    val price = response.body()?.get("price")?.toDoubleOrNull()
//                    if (price != null) {
//                        curprice.text = "$$price USD"
//                        updatePositionPrices(price)
//                    }
//                    curprice.text = "$$price USD"
//
//                    Log.d("MEXCResponse", "Price: $price")
//                } else {
//                    curprice.text = "Failed to fetch price"
//                    Log.e("MEXCResponse", "Error: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
//                curprice.text = "Error"
//                Log.e("MEXCResponse", "Failure: ${t.message}")
//            }
//        })
//    }

//    private fun updatePositionPrices(currentPrice: Double) {
//        // Update each position's price dynamically in the adapter
//        positions.forEachIndexed { index, position ->
//            // Update the price only for relevant positions
//            adapter.updateCurrentPrice(index, currentPrice)
//        }
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        handler.removeCallbacksAndMessages(null) // Stop updates when activity is destroyed
//    }

