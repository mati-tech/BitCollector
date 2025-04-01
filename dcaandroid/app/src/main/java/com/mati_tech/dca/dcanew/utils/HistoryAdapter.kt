package com.mati_tech.dca.dcanew.utils

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mati_tech.dca.R
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.viewmodel.HistoryViewModel
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val viewModel: HistoryViewModel,

) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var positions = mutableListOf<PositionEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(positions[position])
    }

    override fun getItemCount(): Int = positions.size

    fun updateData(newList: List<PositionEntity>) {
        positions = newList.toMutableList()
        notifyDataSetChanged()
    }

    class HistoryViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvCoin: TextView = itemView.findViewById(R.id.tvCoinHistory)
        private val tvBuyPrice: TextView = itemView.findViewById(R.id.tvBuyPriceHistory)
        private val buyDate: TextView = itemView.findViewById(R.id.buyDateHistory)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmountHistory)
        private val tvTotalUsd: TextView = itemView.findViewById(R.id.tvTotalUsdHistory)
        private val sellPrice: TextView = itemView.findViewById(R.id.sellPriceHistory)
        private val sellDate: TextView = itemView.findViewById(R.id.sellDateHistory)
        private val tvProfitLoss: TextView = itemView.findViewById(R.id.tvProfitLossHistory)

        private fun formatDate(timestamp: Long?): String {
            return if (timestamp != null) {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                sdf.format(Date(timestamp))
            } else {
                "-"
            }
        }

        fun bind(position: PositionEntity) {
            tvCoin.text = "Coin: ${position.coin}"
            tvBuyPrice.text = "Buy Price: ${position.buyPrice}"
            buyDate.text = "Buy Date: \n${formatDate(position.buytimestamp)}"
            tvAmount.text = "Amount: ${position.amount}"
            tvTotalUsd.text = "Total (USD): ${position.valueUsd}"
            sellPrice.text = "Sell Price: ${position.sellPrice}"
            sellDate.text = "Sell Date: \n${formatDate(position.selltimestamp)}"
            tvProfitLoss.text = "Profit/Loss: ${position.positionPnl}"

            // Set text color based on profit/loss
            tvProfitLoss.setTextColor(if (position.positionPnl < 0) Color.RED else Color.GREEN)
        }

    }
}
