package com.mati_tech.dca.dcanew

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mati_tech.dca.databinding.ActivityDcacalculatorBinding
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.remote.CryptoResponse

import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel

import com.mati_tech.dca.dcanew.repos.PositionRepos
import com.mati_tech.dca.dcanew.ui.PositionAdapter
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModelFactory
import com.mati_tech.dca.olddca.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class dcaCalculator : AppCompatActivity() {
    private lateinit var viewModel: PositionViewModel
    private lateinit var adapter: PositionAdapter
    private lateinit var binding: ActivityDcacalculatorBinding
    private lateinit var repos: PositionRepos

//
    private val pricesList = mutableListOf<CryptoResponse>()
    private var btcPrice : Double = 0.0
    private var ethPrice : Double = 0.0
    private var ltcPrice : Double = 0.0
    private var adaPrice : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDcacalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repos = PositionRepos(applicationContext)
        val factory = PositionViewModelFactory(repos)
        viewModel = ViewModelProvider(this, factory).get(PositionViewModel::class.java)
        // Observe LiveData from ViewModel

        viewModel.fetchPositions()
        viewModel.cryptoPrices.observe(this, Observer { prices ->

            if (prices.isNotEmpty()){
                pricesList.clear()
                pricesList.addAll(prices)

                btcPrice = pricesList.find { it.symbol.contains("BTC") }?.price?.toDouble() ?: 0.0
                ethPrice = pricesList.find { it.symbol.contains("ETH") }?.price?.toDouble() ?: 0.0
                ltcPrice = pricesList.find { it.symbol.contains("LTC") }?.price?.toDouble() ?: 0.0
                adaPrice = pricesList.find { it.symbol.contains("ADA") }?.price?.toDouble() ?: 0.0

                binding.currentprice.text = "BTC: $btcPrice$ LTC:$ltcPrice$\nETH:$ethPrice\$ ADA:$adaPrice$"
                adapter.updateCurrentPrices(btcPrice, ethPrice, ltcPrice, adaPrice)

            }
            else {
                // Handle no data or error scenario
                binding.currentprice.text =  "BTC: 0$ ETH:0$\nLTC:0$ ADA:0$"
            }
        })




        val recyclerView = binding.PositionsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PositionAdapter(viewModel,
            onCloseatClick = { position ->
//            viewModel.closePosition(position.id, repos.getCurrentPriceFromApi(position.coin))
                showClosePositionDialog(position)
            },
            onDeleteClick = { position ->
//                viewModel.closePosition(position.id,)
            })

        recyclerView.adapter = adapter
        viewModel.positions.observe(this, { positions ->
            adapter.updateData(positions)
        })

        setupSpinner()
        setupInputListeners()

        binding.btnAddPosition.setOnClickListener { addPosition() }
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
                viewModel.closePosition(position.id, enteredPrice)
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
        val coins = listOf("BTC", "LTC", "ETH", "XRP", "ADA", "DOT")
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
    }
    override fun onResume() {
        super.onResume()
        val cryptoList = listOf("BTCUSDT", "ETHUSDT", "LTCUSDT", "ADAUSDT")
        viewModel.startFetchingPrices(cryptoList) // Starts fetching price every second
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopFetchingPrice() // Stops fetching when app is in background
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

