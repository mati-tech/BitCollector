package com.mati_tech.dca.dcanew.ui

import android.graphics.Color
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
import com.mati_tech.dca.olddca.PositionModel

class PositionAdapter(
    private val viewModel: PositionViewModel, // Pass ViewModel to Adapter
    private val onCloseatClick: (PositionEntity) -> Unit,
    private val onDeleteClick: (PositionEntity) -> Unit
) : RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {

    private var positions = mutableListOf<PositionEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return PositionViewHolder(view, onDeleteClick, onCloseatClick, viewModel)
    }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        holder.bind(positions[position])
    }

    override fun getItemCount(): Int = positions.size

//    fun updateCurrentPrice(position: Int, newPrice: Double) {
//        // Update the currentPrice directly in the model
//        positions[position].currentPrice = newPrice
//        notifyItemChanged(position)  // Only update the specific item
//    }

    private val currentPrices = mutableMapOf<Int, Double>() // Store fetched prices

    fun updateCurrentPrice(position: Int, newPrice: Double) {
        if (position in positions.indices) { // Avoid index out of bounds
            currentPrices[position] = newPrice // Store price in map
            notifyItemChanged(position)  // Update only the affected item
        }
    }

    fun findTotalPnl(positions: List<PositionEntity>): Double {
        // Get the current list safely
        var totalPnl = 0.0

        for (position in positions) {
            val totalCost = position.buyPrice * position.amount
            val profitLoss = (position.currentPrice * position.amount) - totalCost
            totalPnl += profitLoss
        }
        return totalPnl
    }


    fun updateData(newList: List<PositionEntity>) {
        positions = newList.toMutableList()
        notifyDataSetChanged()
    }

    class PositionViewHolder(
        itemView: View,
        private val onDeleteClick: (PositionEntity) -> Unit,
        private val onCloseatClick: (PositionEntity) -> Unit,
        private val viewModel: PositionViewModel // Pass ViewModel here
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvCoin: TextView = itemView.findViewById(R.id.tvCoin)
        private val tvBuyPrice: TextView = itemView.findViewById(R.id.tvBuyPrice)
        private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCoinCurrent)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvTotalCost: TextView = itemView.findViewById(R.id.tvTotalCost)
        private val tvProfitLoss: TextView = itemView.findViewById(R.id.tvProfitLoss)
        private val btnDelete: ImageView = itemView.findViewById(R.id.closeorder)
        private val closeat: TextView  = itemView.findViewById(R.id.closePositionat)

        fun bind(position: PositionEntity) {
            val totalCost = position.buyPrice * position.amount
//            val profitLoss = (currentPrice * position.amount) - totalCost
//
//            val currentPrice = currentPrices[adapterPosition] ?: 0.0
            tvCoin.text = position.coin
            tvBuyPrice.text = "Buy Price: $${position.buyPrice}"
            tvAmount.text = "Amount: ${position.amount} ${position.coin}"
            tvTotalCost.text = "Total: $${String.format("%.2f", totalCost)}"

//            tvCurrentPrice.text = if (currentPrice > 0) {
//                "Current Price: $${String.format("%.2f", currentPrice)}"
//            } else {
//                "Loading..." // Show loading until API fetches price
//            }  // Placeholder before fetching price


//            // Fetch current price dynamically and update UI
//            viewModel.fetchCurrentPrice(position) { currentPrice ->
//                val profitLoss = (currentPrice * position.amount) - totalCost
//                tvCurrentPrice.text = "Current Price: $${String.format("%.2f", currentPrice)}"
//                tvProfitLoss.text = "P/L: $${String.format("%.2f", profitLoss)}"
//            }

// Fetch current price from API and update UI dynamically
//            viewModel.fetchCurrentPrice(position) { currentPrice ->
//                val profitLoss = (currentPrice * position.amount) - totalCost
//                tvCurrentPrice.text = "Current Price: $${String.format("%.2f", currentPrice)}"
//                tvProfitLoss.text = "P/L: $${String.format("%.2f", profitLoss)}"
//
//                // Change color based on profit/loss
//                if (profitLoss < 0) {
//                    tvProfitLoss.setTextColor(Color.parseColor("#FF0000")) // Red for loss
//                } else {
//                    tvProfitLoss.setTextColor(Color.parseColor("#8BC34A")) // Green for profit
//                }
//            }


            closeat.setOnClickListener {
                onCloseatClick(position)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(position)
            }
        }
    }
}

