////package com.mati_tech.dca.dcanew.utils
////
////import android.graphics.Color
////import android.view.LayoutInflater
////import android.view.View
////import android.view.ViewGroup
////import android.widget.TextView
////import androidx.recyclerview.widget.RecyclerView
////import com.mati_tech.dca.dcanew.dao.PositionDetailEntity
////import com.mati_tech.dca.R  // Ensure you have this import for your resources
////
////class PositionDetailAdapter() : RecyclerView.Adapter<PositionDetailAdapter.PDetailViewHolder>() {
////
////
////    // ViewHolder to hold the references to the views
////    inner class PDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
////        val tradeType: TextView = itemView.findViewById(R.id.tvTradeType)
////        val tradeAmount: TextView = itemView.findViewById(R.id.et_trade_amount)
////        val tradePrice: TextView = itemView.findViewById(R.id.et_trade_price)
////    }
////
////    // Inflating the layout for the item and returning the ViewHolder
////    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PDetailViewHolder {
////        val view = LayoutInflater.from(parent.context)
////            .inflate(R.layout.positiondetailitem, parent, false)
////        return PDetailViewHolder(view)
////    }
////
////    // Binding data to the views in the ViewHolder
////    override fun onBindViewHolder(holder: PDetailViewHolder, position: Int) {
////        val trade = trades[position]
////
////        // Set the trade type, amount, and price
////        holder.tradeType.text = trade.tradeType
////        holder.tradeAmount.text = "Amount: ${trade.tradeAmount} LTC"
////        holder.tradePrice.text = "Price: $${trade.tradePrice}"
////
////        // Set color for BUY or SELL trade type (green for BUY, red for SELL)
////        holder.tradeType.setTextColor(
////            if (trade.tradeType.equals("BUY", true)) Color.GREEN else Color.RED
////        )
////    }
////
////    // Return the number of items in the list
////    override fun getItemCount(): Int = trades.size
////}
//
//
//package com.mati_tech.dca.dcanew.utils
//
//import android.graphics.Color
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.mati_tech.dca.dcanew.dao.PositionDetailEntity
//import com.mati_tech.dca.R
//
//class PositionDetailAdapter(private val trades: List<PositionDetailEntity>) : RecyclerView.Adapter<PositionDetailAdapter.PDetailViewHolder>() {
//
//    // ViewHolder to hold the references to the views
//    inner class PDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tradeType: TextView = itemView.findViewById(R.id.tvTradeType)
//        val tradeAmount: TextView = itemView.findViewById(R.id.tvTradeAmountitem)  // Assuming correct ID for trade amount
//        val tradePrice: TextView = itemView.findViewById(R.id.tvBuyPriceitem)  // Assuming correct ID for trade price
//        val totalCost: TextView = itemView.findViewById(R.id.tvTradePriceitem)  // Assuming correct ID for total cost
//    }
//
//    // Inflating the layout for the item and returning the ViewHolder
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PDetailViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.positiondetailitem, parent, false)
//        return PDetailViewHolder(view)
//    }
//
//    // Binding data to the views in the ViewHolder
//    override fun onBindViewHolder(holder: PDetailViewHolder, position: Int) {
//        val trade = trades[position]
//
//        // Set the trade type, amount, price, and total cost
//        holder.tradeType.text = trade.tradeType
//        holder.tradeAmount.text = "Amount: ${trade.tradeAmount}"
//        holder.tradePrice.text = "Price: $${trade.tradePrice}"
//        holder.totalCost.text = "Total Cost: $${trade.totalCost}"
//
//        // Set color for BUY or SELL trade type (green for BUY, red for SELL)
//        holder.tradeType.setTextColor(
//            if (trade.tradeType.equals("BUY", true)) Color.GREEN else Color.RED
//        )
//    }
//
//    // Return the number of items in the list
//    override fun getItemCount(): Int = trades.size
//}

//package com.mati_tech.dca.dcanew.utils
//
//import android.graphics.Color
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.mati_tech.dca.dcanew.dao.PositionDetailEntity
//import com.mati_tech.dca.R
//
//class PositionDetailAdapter(
//    private val trades: List<PositionDetailEntity>, // List of PositionDetailEntity
//    private val onItemClick: (PositionDetailEntity) -> Unit // Listener for item clicks
//) : RecyclerView.Adapter<PositionDetailAdapter.PDetailViewHolder>() {
//
//    // ViewHolder to hold the references to the views
//    inner class PDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tradeType: TextView = itemView.findViewById(R.id.tvTradeType)
//        val tradeAmount: TextView = itemView.findViewById(R.id.tvTradeAmountitem)
//        val tradePrice: TextView = itemView.findViewById(R.id.tvBuyPriceitem)
//        val totalCost: TextView = itemView.findViewById(R.id.tvTradePriceitem)
//    }
//
//    // Inflating the layout for the item and returning the ViewHolder
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PDetailViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.positiondetailitem, parent, false)
//        return PDetailViewHolder(view)
//    }
//
//    // Binding data to the views in the ViewHolder
//    override fun onBindViewHolder(holder: PDetailViewHolder, position: Int) {
//        val trade = trades[position]
//
//        // Set the trade type, amount, price, and total cost
//        holder.tradeType.text = trade.tradeType
//        holder.tradeAmount.text = "Amount: ${trade.tradeAmount}"
//        holder.tradePrice.text = "Price: $${trade.tradePrice}"
//        holder.totalCost.text = "Total Cost: $${trade.totalCost}"
//
//        // Set color for BUY or SELL trade type (green for BUY, red for SELL)
//        holder.tradeType.setTextColor(
//            if (trade.tradeType.equals("BUY", true)) Color.GREEN else Color.RED
//        )
//
//        // Set item click listener to handle the click on the item
//        itemView.setOnClickListener {
//            onItemClick(trade) // Trigger the click listener with the specific trade
//        }
//    }
//
//    // Return the number of items in the list
//    override fun getItemCount(): Int = trades.size
//
//    // Optional: method to update the data in the adapter if needed
//    fun updateData(newTrades: List<PositionDetailEntity>) {
//        // Update the trades list and notify the adapter of the change
//        trades = newTrades
//        notifyDataSetChanged()
//    }
//}
//package com.mati_tech.dca.dcanew.utils
//
//import android.graphics.Color
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.lifecycle.LiveData
//import androidx.recyclerview.widget.RecyclerView
//import com.mati_tech.dca.R
//import com.mati_tech.dca.dcanew.dao.PositionDetailEntity
//import com.mati_tech.dca.dcanew.viewmodel.PositionDetailViewModel
//
//class PositionDetailAdapter(
//    viewmodel: PositionDetailViewModel) :
//    RecyclerView.Adapter<PositionDetailAdapter.PositionDetailViewHolder>() {
//
//    private var trades = mutableListOf<PositionDetailEntity>()
////    private var trades = LiveData<List<PositionDetailEntity>>()
//    private val currentPrice = mutableMapOf<String, Double>() // Coin -> Price mapping
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionDetailViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.positiondetailitem, parent, false)
//        return PositionDetailViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: PositionDetailViewHolder, position: Int) {
//        holder.bind(trades[position])
//    }
//
//    override fun getItemCount(): Int = trades.size
//
////    fun updateData(newTradesList: List<PositionDetailEntity>) {
////        trades = newTradesList.toMutableList()
////        notifyDataSetChanged()
////    }
//    // Example method to update the adapter
//fun updateData(newTradesList: List<PositionDetailEntity>) {
//    trades = newTradesList.toMutableList()
//    notifyDataSetChanged()
//}
//
//    //    fun updateData(newTrades: List<PositionDetailEntity>) {
////        trades.clear()
////        trades.addAll(newTrades)
////        notifyDataSetChanged()
////    }
////    fun getCurrentPrice(coin: String): Double {
////        return currentPrice[coin] ?: 0.0
////    }
////
////    fun updateCurrentPrices(coin: Double ) {
////        // Update the prices in the currentPrices map based on the coin symbols
////        currentPrice["COIN"] = coin
////        // Notify the adapter to refresh the data
////        notifyDataSetChanged()
////    }
//    // ViewHolder class to hold the views
//    class PositionDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val tradeType: TextView = itemView.findViewById(R.id.tvTradeType)
//        private val tradeAmount: TextView = itemView.findViewById(R.id.tvTradeAmountitem)
//        private val tradePrice: TextView = itemView.findViewById(R.id.tvBuyPriceitem)
//        private val totalCost: TextView = itemView.findViewById(R.id.tvTradePriceitem)
//
//        fun bind(trade: PositionDetailEntity) {
//
//            tradeType.text = trade.tradeType
//            tradeAmount.text = "Amount: ${trade.tradeAmount}"
//            tradePrice.text = "Price: $${trade.tradePrice}"
//            totalCost.text = "Total Cost: $${trade.totalCost}"
//
//            // Change text color based on trade type (green for BUY, red for SELL)
//            tradeType.setTextColor(
//                if (trade.tradeType.equals("BUY", true)) Color.GREEN else Color.RED
//            )
//
////
//        }
//    }
//}


package com.mati_tech.dca.dcanew.utils

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mati_tech.dca.R
import com.mati_tech.dca.dcanew.dao.PositionDetailEntity

class PositionDetailAdapter (private val onTradeClickListener: OnTradeClickListener? = null) : RecyclerView.Adapter<PositionDetailAdapter.PositionDetailViewHolder>() {

    private var trades = listOf<PositionDetailEntity>()
    interface OnTradeClickListener {

        fun onTradeLongClick(trade: PositionDetailEntity)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.positiondetailitem, parent, false)
//        return PositionDetailViewHolder(view)
        return PositionDetailViewHolder(view, onTradeClickListener)
    }

    override fun onBindViewHolder(holder: PositionDetailViewHolder, position: Int) {
        holder.bind(trades[position])
    }

    override fun getItemCount(): Int = trades.size

    fun submitList(newTrades: List<PositionDetailEntity>) {
        trades = newTrades
        notifyDataSetChanged()
    }

    class PositionDetailViewHolder(itemView: View,
                                   private val onTradeClickListener: OnTradeClickListener?) : RecyclerView.ViewHolder(itemView) {
        private val tradeType: TextView = itemView.findViewById(R.id.tvTradeType)
        private val tradeAmount: TextView = itemView.findViewById(R.id.tvTradeAmountitem)
        private val tradePrice: TextView = itemView.findViewById(R.id.tvBuyPriceitem)
        private val totalCost: TextView = itemView.findViewById(R.id.tvTradePriceitem)

        fun bind(trade: PositionDetailEntity) {
            tradeType.text = trade.tradeType
            tradeAmount.text = "Amount: %.4f".format(trade.tradeAmount)
            tradePrice.text = "Price: $%.2f".format(trade.tradePrice)
            totalCost.text = "Total: $%.2f".format(trade.totalCost)
            tradeType.setTextColor(
                if (trade.tradeType.equals("BUY", true)) Color.GREEN else Color.RED
            )
            // Set long-click listener
            itemView.setOnLongClickListener {
                onTradeClickListener?.onTradeLongClick(trade)
                true // Consume the long-click event
            }
        }
    }
}