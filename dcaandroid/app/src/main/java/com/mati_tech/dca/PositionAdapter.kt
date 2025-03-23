package com.mati_tech.dca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PositionAdapter(
    private val onDeleteClick: (PositionModel) -> Unit // ✅ Fix: Add this constructor parameter
) : RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {

    private var positions = mutableListOf<PositionModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return PositionViewHolder(view, onDeleteClick) // ✅ Fix: Pass `onDeleteClick`
    }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        holder.bind(positions[position], positions[position].currentPrice)
    }

    override fun getItemCount(): Int {
        return positions.size
    }
    // Method to update the current price for a specific position
    fun updateCurrentPrice(position: Int, newPrice: Double) {
        // Update the currentPrice directly in the model
        positions[position].currentPrice = newPrice
        notifyItemChanged(position)  // Only update the specific item
    }

    // To update the positions list
    fun updateData(newList: List<PositionModel>) {
        positions = newList.toMutableList()
        notifyDataSetChanged() // Manually notify the adapter about data change
    }

    class PositionViewHolder(
        itemView: View,
        private val onDeleteClick: (PositionModel) -> Unit // ✅ Fix: Pass it correctly
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvCoin: TextView = itemView.findViewById(R.id.tvCoin)
        private val tvBuyPrice: TextView = itemView.findViewById(R.id.tvBuyPrice)
        private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCoinCurrent)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvTotalCost: TextView = itemView.findViewById(R.id.tvTotalCost)
        private val tvProfitLoss: TextView = itemView.findViewById(R.id.tvProfitLoss)
        private val btnDelete: ImageView = itemView.findViewById(R.id.closeorder)

        fun bind(positionModel: PositionModel, currentPrice: Double) {
            val totalCost = positionModel.buyPrice * positionModel.amount
            val profitLoss = (currentPrice * positionModel.amount) - totalCost

            tvCoin.text = positionModel.coin
            tvBuyPrice.text = "Buy Price: $${positionModel.buyPrice}"
            tvAmount.text = "Amount: ${positionModel.amount} ${positionModel.coin}"
            tvTotalCost.text = "Total: $${String.format("%.2f", totalCost)}"
            tvCurrentPrice.text = "Current Price: $${String.format("%.2f", currentPrice)}"
            tvProfitLoss.text = "P/L: $${String.format("%.2f", profitLoss)}"

            btnDelete.setOnClickListener {
                onDeleteClick(positionModel) // ✅ Now correctly references the function
            }
        }
    }
}
