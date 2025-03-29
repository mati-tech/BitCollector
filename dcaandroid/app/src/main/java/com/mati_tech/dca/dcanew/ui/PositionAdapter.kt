package com.mati_tech.dca.dcanew.ui

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.mati_tech.dca.R
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel

class PositionAdapter(
    private val viewModel: PositionViewModel, // Pass ViewModel to Adapter
    private val onCloseatClick: (PositionEntity) -> Unit,
    private val onDeleteClick: (PositionEntity) -> Unit
) : RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {

    private var positions = mutableListOf<PositionEntity>()
    private val currentPrices = mutableMapOf<String, Double>() // Coin -> Price mapping

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return PositionViewHolder(view,this,  onDeleteClick, onCloseatClick, viewModel)
    }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        holder.bind(positions[position])
    }

    override fun getItemCount(): Int = positions.size

    fun getCurrentPrice(coin: String): Double {
        return currentPrices[coin] ?: 0.0
    }



    fun updateData(newList: List<PositionEntity>) {
        positions = newList.toMutableList()
        notifyDataSetChanged()
    }

//    fun updateCurrentPrices(priceList: List<CryptoResponse>) {
//        for (price in priceList) {
//            currentPrices[price.symbol] = price.price.toDouble()  // Store latest price
//        }
////        cryptoList.forEach { crypto ->
////            println("Updating price: ${crypto.symbol} -> ${crypto.price}") // Debug
////        }
//        notifyDataSetChanged()
//    }
// Method to update the current prices for each cryptocurrency
// Method to update the current prices for each cryptocurrency
fun updateCurrentPrices(btcPrice: Double, ethPrice: Double, ltcPrice: Double, adaPrice: Double) {
    // Update the prices in the currentPrices map based on the coin symbols
    currentPrices["BTC"] = btcPrice
    currentPrices["ETH"] = ethPrice
    currentPrices["LTC"] = ltcPrice
    currentPrices["ADA"] = adaPrice
    // Notify the adapter to refresh the data
    notifyDataSetChanged()
}
    fun findTotalPnl(positions: List<PositionEntity>): Double {
        var totalPnl = 0.0

        // Iterate through each position and calculate its PnL
        for (position in positions) {
            val currentPrice = getCurrentPrice(position.coin)
            if (currentPrice > 0) {
                val totalCost = position.buyPrice * position.amount
                val currentValue = currentPrice * position.amount
                val profitLoss = currentValue - totalCost
                totalPnl += profitLoss
                // Add logging to check values
                Log.d("PositionAdapter", "Calculating PnL for ${position.coin}:")
                Log.d("PositionAdapter", "Buy Price: ${position.buyPrice}, Amount: ${position.amount}")
                Log.d("PositionAdapter", "Current Price: $currentPrice")
                Log.d("PositionAdapter", "Total Cost: $totalCost")
                Log.d("PositionAdapter", "Profit/Loss: $profitLoss")
            }
        }
        // Log the total PnL value
        Log.d("PositionAdapter", "Total PnL: $totalPnl")
        return totalPnl
    }
    class PositionViewHolder(
        itemView: View,
        private val adapter: PositionAdapter,
        private val onDeleteClick: (PositionEntity) -> Unit,
        private val onCloseatClick: (PositionEntity) -> Unit,
        private val viewModel: PositionViewModel // Pass ViewModel here
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvCoin: TextView = itemView.findViewById(R.id.tvCoin)
        private val tvBuyPrice: TextView = itemView.findViewById(R.id.tvBuyPrice)
        private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCoinCurrent)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvTotalUsd: TextView = itemView.findViewById(R.id.tvTotalUsd)
        private val tvProfitLoss: TextView = itemView.findViewById(R.id.tvProfitLoss)
        private val btnDelete: ImageView = itemView.findViewById(R.id.closeorder)
        private val closeat: TextView  = itemView.findViewById(R.id.closePositionat)

        fun bind(position: PositionEntity) {
            val currentPrice = adapter.getCurrentPrice(position.coin)

            tvCoin.text = position.coin
            tvBuyPrice.text = "Buy Price: $${position.buyPrice}"
            tvAmount.text = "Amount: ${position.amount} ${position.coin}"
            tvTotalUsd.text = "Total: $${String.format("%.2f", position.valueUsd)}"

            if (currentPrice > 0) {
                tvCurrentPrice.text = "Current Price: $${String.format("%.2f", currentPrice)}"
            } else {
                tvCurrentPrice.text = "Loading..."
            }

            val profitLoss = (currentPrice * position.amount) - position.valueUsd
            tvProfitLoss.text = "P/L: $${String.format("%.2f", profitLoss)}"

            // Color change based on profit/loss
            tvProfitLoss.setTextColor(
                if (profitLoss < 0) Color.RED else Color.GREEN
            )

            closeat.setOnClickListener {
                onCloseatClick(position)
            }
            btnDelete.setOnClickListener {
                onDeleteClick(position)
            }
        }
    }
}

