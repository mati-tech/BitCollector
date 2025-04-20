package com.mati_tech.dca.dcanew.utils

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.mati_tech.dca.R
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.viewmodel.HistoryViewModel
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val viewModel: HistoryViewModel,
    private val onEditClick: (PositionEntity) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var positions = mutableListOf<PositionEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view, viewModel)
    }
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = positions[position]
        holder.bind(positions[position], onEditClick)
        holder.itemView.setOnLongClickListener{
            AlertDialog.Builder(it.context)
                .setTitle("Delete Position")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes") { _,_ ->
                    viewModel.deletePosition(item)
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }

    override fun getItemCount(): Int = positions.size

    fun updateData(newList: List<PositionEntity>) {
        positions = newList.toMutableList()
        notifyDataSetChanged()
    }

    class HistoryViewHolder(
        itemView: View,
        private val viewModel: HistoryViewModel,
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvCoin: TextView = itemView.findViewById(R.id.tvCoinHistory)
        private val tvBuyPrice: TextView = itemView.findViewById(R.id.tvBuyPriceHistory)
        private val buyDate: TextView = itemView.findViewById(R.id.buyDateHistory)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmountHistory)
        private val tvTotalUsd: TextView = itemView.findViewById(R.id.tvTotalUsdHistory)
        private val sellPrice: TextView = itemView.findViewById(R.id.sellPriceHistory)
        private val sellDate: TextView = itemView.findViewById(R.id.sellDateHistory)
        private val tvProfitLoss: TextView = itemView.findViewById(R.id.tvProfitLossHistory)
        private val editbtn: ImageView = itemView.findViewById(R.id.edit_position_history)
        private val detailed: TextView = itemView.findViewById(R.id.DetailedStatus)

        private fun formatDate(timestamp: Long?): String {
            return if (timestamp != null) {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                sdf.format(Date(timestamp))
            } else {
                "-"
            }
        }

        fun bind(position: PositionEntity, onEditClick: (PositionEntity) -> Unit) {
            tvCoin.text = "Coin: ${position.coin}"
            tvBuyPrice.text = "Buy Price: ${position.buyPrice}"
            buyDate.text = "Buy Date: \n${formatDate(position.buytimestamp)}"
            tvAmount.text = "Amount: ${position.amount}"
            tvTotalUsd.text = "Total (USD): ${position.valueUsd}"
            sellPrice.text = "Sell Price: ${position.sellPrice}"
            sellDate.text = "Sell Date: \n${formatDate(position.selltimestamp)}"
            tvProfitLoss.text = "Profit/Loss: ${position.positionPnl}"

            if (position.hasDetails){
                detailed.visibility = View.VISIBLE
                detailed.text = "*"
            }
            editbtn.setOnClickListener {
                showEditPositionDialog(itemView.context, position)
            }
            // Set text color based on profit/loss
            tvProfitLoss.setTextColor(if (position.positionPnl < 0) Color.RED else Color.GREEN)
        }

        private fun showEditPositionDialog(context: Context, position: PositionEntity) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogeditposition, null)

            val etNewPrice = dialogView.findViewById<EditText>(R.id.etNewPrice)
            val etNewAmount = dialogView.findViewById<EditText>(R.id.etNewAmount)
            val etNewTotalUsd = dialogView.findViewById<EditText>(R.id.etNewTotalUsd)
            val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
            val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

            // Pre-fill fields with existing values
            etNewPrice.setText(position.buyPrice.toString())
            etNewAmount.setText(position.amount.toString())
            etNewTotalUsd.setText(position.valueUsd.toString())

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)
                .create()

            btnSave.setOnClickListener {
                val newPrice = etNewPrice.text.toString().toDoubleOrNull()
                val newAmount = etNewAmount.text.toString().toDoubleOrNull()
                val newTotalUsd = etNewTotalUsd.text.toString().toDoubleOrNull()

                if (newPrice != null && newAmount != null && newTotalUsd != null) {
                    viewModel.updatePosition(position.id, newPrice, newAmount, newTotalUsd)
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
                }
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}
