package com.mati_tech.dca.dcanew.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mati_tech.dca.databinding.FragmentPositionsBinding
import com.mati_tech.dca.dcanew.db.AppDatabase
import com.mati_tech.dca.dcanew.dao.PositionEntity
import com.mati_tech.dca.dcanew.remoteApi.cryptodata.CryptoResponse
import com.mati_tech.dca.dcanew.remoteApi.repos.PositionRepos
import com.mati_tech.dca.dcanew.ui.activities.PositionDetail
import com.mati_tech.dca.dcanew.utils.PositionAdapter
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModelFactory


class FragmentPositions : Fragment() {
    private lateinit var binding: FragmentPositionsBinding

    private lateinit var viewModel: PositionViewModel
    private lateinit var adapter: PositionAdapter
    private lateinit var repos: PositionRepos

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
    private var enaPrice : Double = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPositionsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = AppDatabase.getDatabase(requireContext())

        repos = PositionRepos(db.positionDao())
        val factory = PositionViewModelFactory(repos)
        viewModel = ViewModelProvider(this, factory).get(PositionViewModel::class.java)
        // Observe LiveData from ViewModel
        viewModel.fetchPositions()

        viewModel.cryptoPrices.observe(viewLifecycleOwner) { prices ->

            println("Crypto Prices in OngoingPositions: $prices")
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
                aptPrice = pricesList.find { it.symbol.contains("APTUSDT") }?.price?.toDouble() ?: 0.0
                enaPrice = pricesList.find { it.symbol.contains("ENAUSDT") }?.price?.toDouble() ?: 0.0

//                binding.currentprice.text =
//                    "BTC: $btcPrice$ ---- LTC: $ltcPrice$\n"+
//                            "ETH: $ethPrice$ ---- ADA: $adaPrice$\n" +
//                            "DOT: $dotPrice$ ---- AVAX: $avaxPrice$\n" +
//                            "XRP: $xrpPrice$ ---- ARB: $arbPrice$\n" +
//                            "SOL: $solPrice\$"

                adapter.updateCurrentPrices(btcPrice, ethPrice, ltcPrice, adaPrice, dotPrice, avaxPrice, arbPrice, xrpPrice, solPrice, aptPrice, enaPrice)


//                updatepnl()
            }
            else {
                println("not getting data")
                // Handle no data or error scenario
//                binding.currentprice.text =  "Network Error!"
            }

        }

        val recyclerView = binding.PositionsRecyclerViewOnGoingPositions
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

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
            onItemClick = { position ->
                // Handle item click
                Toast.makeText(requireContext(), "Clicked on: ${position.coin}", Toast.LENGTH_SHORT).show()
                val intent = Intent ( requireContext(), PositionDetail::class.java)
                intent.putExtra("coin_id", position.id)
                intent.putExtra("coin_name", position.coin)
                intent.putExtra("total_amount", position.amount.toString())
                intent.putExtra("avg_buy_price", position.buyPrice.toString())
//                intent.putExtra("realized_pnl", position.realizedPnL.toString())
//                intent.putExtra("unrealized_pnl", position.unrealizedPnL.toString())
                startActivity(intent)
                // Or navigate to details screen
            }

        )
        recyclerView.adapter = adapter
        viewModel.positions.observe(viewLifecycleOwner) { positions ->
            adapter.updateData(positions)
        }
    }

    private fun showDeletePositionConfirmationBox(position: PositionEntity) {
        val builder = AlertDialog.Builder(requireContext())
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

    private fun showClosePositionDialog(position: PositionEntity) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Close Position")
        builder.setMessage("Enter the closing price for ${position.coin}")

        // Create an EditText for user input
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
                val formattedPnl = String.format("%.2f", pnl).toDouble()
                val formattedEnteredPrice = String.format("%.2f", enteredPrice).toDouble()
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

    override fun onResume() {
        super.onResume()
        val cryptoList = listOf("BTCUSDT", "ETHUSDT", "LTCUSDT", "ADAUSDT", "XRPUSDT", "DOTUSDT", "SOLUSDT", "AVAXUSDT", "ARBUSDT", "APTUSDT", "ENAUSDT")
        viewModel.startFetchingPrices(cryptoList) // Starts fetching price every second
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopFetchingPrice() // Stops fetching when app is in background
    }
}
