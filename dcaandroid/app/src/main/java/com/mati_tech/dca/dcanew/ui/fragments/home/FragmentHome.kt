package com.mati_tech.dca.dcanew.ui.fragments.home

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.mati_tech.dca.databinding.FragmentHomeBinding
import com.mati_tech.dca.dcanew.db.AppDatabase
import com.mati_tech.dca.dcanew.dao.PositionEntity
import com.mati_tech.dca.dcanew.remoteApi.client.ApiClient
import com.mati_tech.dca.dcanew.remoteApi.cryptodata.CryptoResponse
import com.mati_tech.dca.dcanew.remoteApi.repos.PositionRepos
import com.mati_tech.dca.dcanew.remoteApi.repos.UserRepos
import com.mati_tech.dca.dcanew.ui.activities.OngoingPositions
import com.mati_tech.dca.dcanew.ui.activities.UserProfile
import com.mati_tech.dca.dcanew.ui.activities.activitiesUI.AuthActivityUI
import com.mati_tech.dca.dcanew.ui.activities.activitiesUI.MainActivityUI
import com.mati_tech.dca.dcanew.utils.PositionAdapter
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModelFactory
import com.mati_tech.dca.dcanew.viewmodel.UserViewModel
import com.mati_tech.dca.dcanew.viewmodel.UserViewModelFactory


class FragmentHome : Fragment() {
    private lateinit var viewModel: PositionViewModel
    private lateinit var userviewModel: UserViewModel
    private lateinit var adapter: PositionAdapter
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: FragmentHomeBinding
    private lateinit var repos: PositionRepos

    private lateinit var token :String
    private lateinit var email : String

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
    private var enaPrice: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        token = sharedPreferences.getString("AUTH_TOKEN", "-")!!
        email = sharedPreferences.getString("email", "-")!!



        if (token == null) {
            // No token found, user is not logged in, redirect to Login screen
            val intent = Intent(requireContext(), AuthActivityUI::class.java)
            startActivity(intent)
            activity?.finish()  // Finish MainActivity to prevent going back to the logged-in screen
        } else {
            // Token exists, user is logged in, continue to the main screen
            // This can be where you set up your main UI or dashboard
        }

        val repos = UserRepos(ApiClient.userApiService)
        val factory = UserViewModelFactory(repos)
        userviewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getDatabase(requireContext())
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        defineUser(token,  email)
        var balance = sharedPreferences.getFloat("balance", 0.0f)
        val formattedBalance = String.format("%.4f", balance)
        binding.TotalMoneyInCrypto.text = "Total Balance: $formattedBalance$"

        repos = PositionRepos(db.positionDao())
        val factory = PositionViewModelFactory(repos)
        viewModel = ViewModelProvider(this, factory).get(PositionViewModel::class.java)

        viewModel.fetchPositions()
        viewModel.cryptoPrices.observe(viewLifecycleOwner) { prices ->

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
                enaPrice = pricesList.find { it.symbol.contains("ENAUSDT")  }?.price?.toDouble() ?: 0.0


                binding.currentprice.text =
                    "BTC: $btcPrice$ ---- LTC: $ltcPrice$\n"+
                            "ETH: $ethPrice$ -------- ADA: $adaPrice$\n" +
                            "DOT: $dotPrice$ -------- AVAX: $avaxPrice$\n" +
                            "XRP: $xrpPrice$ -------- ARB: $arbPrice$\n" +
                            "SOL: $solPrice$ -------- APT: $aptPrice$\n" +
                            "ENA: $enaPrice"

                adapter.updateCurrentPrices(btcPrice, ethPrice, ltcPrice, adaPrice, dotPrice, avaxPrice, arbPrice, xrpPrice, solPrice, aptPrice, enaPrice)

                updatepnl()
                updateCurrentValue()
            }
            else {
                // Handle no data or error scenario
                binding.currentprice.text =  "Network Error!"
            }
        }
        adapter = PositionAdapter(viewModel,
            onCloseatClick = { position ->

                showClosePositionDialog(position)
            },
            onDeleteClick = { position ->
                showDeletePositionConfirmationBox(position)

            },
            onItemClick = {
                    position ->
            })
        viewModel.totalInvested.observe(viewLifecycleOwner) { total ->
            val balance = sharedPreferences.getFloat("balance", 0.0f)
            binding.TotalMoneyUsd.text = "In Positions: ${String.format("%.4f", total)}$"
            val availableMoney = balance - total
            binding.TotalFreeMoney.text = "Available balance:\n ${String.format("%.4f", availableMoney)}$"
        }
        viewModel.fetchTotalInvested()
        setupSpinner()
        setupInputListeners()
        updateCurrentValue()
//        binding.btnAddPosition.setOnClickListener {
//            addPosition()
//        }
//        binding.historyButton.setOnClickListener{
//            val intent = Intent(this, PositionHistory::class.java)
//
//            startActivity(intent)
//        }
        binding.btnCurrentPositions.setOnClickListener {
            val intent = Intent(requireContext(), OngoingPositions::class.java)
            startActivity(intent)
        }
        binding.btnCurrentPositions.setOnLongClickListener {
            val intent = Intent(requireContext(), MainActivityUI::class.java)
            startActivity(intent)
            true
        }
        binding.profileIcon.setOnClickListener {
            val intent = Intent(requireContext(), UserProfile::class.java)
            startActivity(intent)
        }
    }

    private fun defineUser(token: String, email: String) {
        userviewModel.fetchUserByEmail(token, email)
        userviewModel.userDetails.observe(viewLifecycleOwner){user ->
            if (user != null){
                val editor = sharedPreferences.edit()
                user.id?.let { editor.putInt("UID", it) }
                editor.putString("FULLNAME", user.full_name)
                editor.apply()
            }
        }
    }


    private fun showDeletePositionConfirmationBox(position: PositionEntity) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Close Position")
        builder.setMessage("Are you sure of closing position for: ${position.coin}")

        // Set up buttons
        builder.setPositiveButton("Confirm") { _, _ ->
            val currentPrice = adapter.getCurrentPrice(position.coin) // Get latest price
            val pnl = (currentPrice * position.amount) - (position.buyPrice * position.amount)
            val formattedPnl = String.format("%.4f", pnl).toDouble()
            val formattedCurrentPrice = String.format("%.4f", currentPrice).toDouble()
            viewModel.closePosition(position.id, formattedPnl, formattedCurrentPrice)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun updatepnl() {
        viewModel.positions.observe(viewLifecycleOwner){positions ->
            val totalPnl = adapter.findTotalPnl(positions )
            binding.TotalProfitLoss.text = "Total PnL : ${String.format("%.4f", totalPnl)}$"
            if (totalPnl < 0) {
                binding.TotalProfitLoss.setBackgroundColor(Color.RED) // Red for negative PnL
            } else {
                binding.TotalProfitLoss.setBackgroundColor(Color.GREEN) // Reset for non-negative PnL
            }
        }
    }

    private fun showClosePositionDialog(position: PositionEntity) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Close Position")
        builder.setMessage("Enter the closing price for ${position.coin}")

        val input = EditText(requireContext())
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
                viewModel.closePosition(position.id,formattedPnl,  formattedEnteredPrice)
            } else {
                Toast.makeText(requireContext(), "Invalid price", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun setupSpinner() {
        val coins = listOf("BTC", "LTC", "SOL", "ETH", "XRP", "ADA", "DOT", "AVAX", "ARB", "APT", "ENA")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, coins)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.coinNameSpinner.adapter = adapter

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
            binding.AmountinCoin.setText(String.format("%.4f", ValueUsd / buyPrice))
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
        val cryptoList = listOf("BTCUSDT", "ETHUSDT", "LTCUSDT", "ADAUSDT", "XRPUSDT", "DOTUSDT", "SOLUSDT", "AVAXUSDT", "ARBUSDT", "APTUSDT", "ENAUSDT")
        viewModel.startFetchingPrices(cryptoList) // Starts fetching price every second

        viewModel.fetchTotalInvested()
        var balance = sharedPreferences.getFloat("balance", 0.0f)
        val formattedBalance = String.format("%.4f", balance)
        binding.TotalMoneyInCrypto.text = "Total Balance: $formattedBalance$"
        updateCurrentValue()

    }

    override fun onPause() {
        super.onPause()
        viewModel.stopFetchingPrice() // Stops fetching when app is in background
    }

    private fun updateCurrentValue() {

        viewModel.totalInvested.observe(viewLifecycleOwner) { totalInvested ->
            viewModel.positions.observe(viewLifecycleOwner) { positions ->
                val totalPnl = adapter.findTotalPnl(positions)
                val currentValue = totalInvested + totalPnl
                Log.d("INVEST_DEBUG", "Total Invested: $totalInvested")
                Log.d("PNL_DEBUG", "Total PnL: $totalPnl")
                Log.d("CURRENT_VALUE_DEBUG", "Calculated Current Value: $currentValue")
                binding.TotalCurrentMoneyUsd.text = "Current Balance\nif close all now: ${String.format("%.2f", currentValue)}$"
            }
        }
    }
}