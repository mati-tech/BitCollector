package com.mati_tech.dca.dcanew.utils

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mati_tech.dca.dcanew.db.PositionDetailEntity
import com.mati_tech.dca.R  // Ensure you have this import for your resources

class PositionDetailAdapter(private val trades: List<PositionDetailEntity>) : RecyclerView.Adapter<PositionDetailAdapter.PDetailViewHolder>() {

    // ViewHolder to hold the references to the views
    inner class PDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tradeType: TextView = itemView.findViewById(R.id.tvTradeType)
        val tradeAmount: TextView = itemView.findViewById(R.id.et_trade_amount)
        val tradePrice: TextView = itemView.findViewById(R.id.et_trade_price)
    }

    // Inflating the layout for the item and returning the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.positiondetailitem, parent, false)
        return PDetailViewHolder(view)
    }

    // Binding data to the views in the ViewHolder
    override fun onBindViewHolder(holder: PDetailViewHolder, position: Int) {
        val trade = trades[position]

        // Set the trade type, amount, and price
        holder.tradeType.text = trade.tradeType
        holder.tradeAmount.text = "Amount: ${trade.tradeAmount} LTC"
        holder.tradePrice.text = "Price: $${trade.tradePrice}"

        // Set color for BUY or SELL trade type (green for BUY, red for SELL)
        holder.tradeType.setTextColor(
            if (trade.tradeType.equals("BUY", true)) Color.GREEN else Color.RED
        )
    }

    // Return the number of items in the list
    override fun getItemCount(): Int = trades.size
}
