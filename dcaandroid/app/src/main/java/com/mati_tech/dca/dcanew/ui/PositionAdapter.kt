package com.mati_tech.dca.dcanew.ui

//import android.R
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.LayoutInflater
//import android.view.ViewGroup
//
//import androidx.recyclerview.widget.RecyclerView
//
//import com.mati_tech.dca.dcanew.db.PositionEntity
//import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel
//import com.mati_tech.dca.dcanew.

//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.mati_tech.dca.R
//import com.mati_tech.dca.dcanew.db.PositionEntity
//import com.mati_tech.dca.olddca.PositionModel
//
//class PositionAdapter(
//    private val onDeleteClick: (PositionEntity) -> Unit // ✅ Fix: Add this constructor parameter
//) : RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {
//
//    private var positions = mutableListOf<PositionEntity>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
//        return PositionViewHolder(view, onDeleteClick) // ✅ Fix: Pass `onDeleteClick`
//    }
//
//    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
//        holder.bind(positions[position], positions[position].currentPrice)
//    }
//
//    override fun getItemCount(): Int {
//        return positions.size
//    }
//    // Method to update the current price for a specific position
//    fun updateCurrentPrice(position: Int, newPrice: Double) {
//        // Update the currentPrice directly in the model
//        positions[position].currentPrice = newPrice
//        notifyItemChanged(position)  // Only update the specific item
//    }
//
//    // To update the positions list
//    fun updateData(newList: List<PositionModel>) {
//        positions = newList.toMutableList()
//        notifyDataSetChanged() // Manually notify the adapter about data change
//    }
//
//    class PositionViewHolder(
//        itemView: View,
//        private val onDeleteClick: (PositionModel) -> Unit // ✅ Fix: Pass it correctly
//    ) : RecyclerView.ViewHolder(itemView) {
//
//        private val tvCoin: TextView = itemView.findViewById(R.id.tvCoin)
//        private val tvBuyPrice: TextView = itemView.findViewById(R.id.tvBuyPrice)
//        private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCoinCurrent)
//        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
//        private val tvTotalCost: TextView = itemView.findViewById(R.id.tvTotalCost)
//        private val tvProfitLoss: TextView = itemView.findViewById(R.id.tvProfitLoss)
//        private val btnDelete: ImageView = itemView.findViewById(R.id.closeorder)
//
//        fun bind(positionModel: PositionModel, currentPrice: Double) {
//            val totalCost = positionModel.buyPrice * positionModel.amount
//            val profitLoss = (currentPrice * positionModel.amount) - totalCost
//
//            tvCoin.text = positionModel.coin
//            tvBuyPrice.text = "Buy Price: $${positionModel.buyPrice}"
//            tvAmount.text = "Amount: ${positionModel.amount} ${positionModel.coin}"
//            tvTotalCost.text = "Total: $${String.format("%.2f", totalCost)}"
//            tvCurrentPrice.text = "Current Price: $${String.format("%.2f", currentPrice)}"
//            tvProfitLoss.text = "P/L: $${String.format("%.2f", profitLoss)}"
//
//            btnDelete.setOnClickListener {
//                onDeleteClick(positionModel) // ✅ Now correctly references the function
//            }
//        }
//    }
//}

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mati_tech.dca.R
import com.mati_tech.dca.dcanew.db.PositionEntity
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel

//class PositionAdapter(
//    private val onDeleteClick: (PositionEntity) -> Unit // ✅ Use PositionEntity now
//) : RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {
//
//    private var positions = mutableListOf<PositionEntity>() // ✅ Use PositionEntity
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
//        return PositionViewHolder(view, onDeleteClick)
//    }
//
//    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
//        holder.bind(positions[position])
//    }
//
//    override fun getItemCount(): Int = positions.size
//
//    // ✅ Update the current price in Room database
//    fun updateCurrentPrice(position: Int, newPrice: Double) {
//        positions[position].currentPrice = newPrice
//        notifyItemChanged(position)
//    }
//
//    // ✅ Update the adapter's list when ViewModel LiveData changes
//    fun updateData(newList: List<PositionEntity>) {
//        positions.clear()
//        positions.addAll(newList)
//        notifyDataSetChanged()
//    }
//
//    class PositionViewHolder(
//        itemView: View,
//        private val onDeleteClick: (PositionEntity) -> Unit // ✅ Pass PositionEntity
//    ) : RecyclerView.ViewHolder(itemView) {
//
//        private val tvCoin: TextView = itemView.findViewById(R.id.tvCoin)
//        private val tvBuyPrice: TextView = itemView.findViewById(R.id.tvBuyPrice)
//        private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCoinCurrent)
//        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
//        private val tvTotalCost: TextView = itemView.findViewById(R.id.tvTotalCost)
//        private val tvProfitLoss: TextView = itemView.findViewById(R.id.tvProfitLoss)
//        private val btnDelete: ImageView = itemView.findViewById(R.id.closeorder)
//
//        fun bind(position: PositionEntity) {
//            val totalCost = position.buyPrice * position.amount
//            val profitLoss = (position.currentPrice * position.amount) - totalCost
//
//            tvCoin.text = position.coin
//            tvBuyPrice.text = "Buy Price: $${position.buyPrice}"
//            tvAmount.text = "Amount: ${position.amount} ${position.coin}"
//            tvTotalCost.text = "Total: $${String.format("%.2f", totalCost)}"
//            tvCurrentPrice.text = "Current Price: $${String.format("%.2f", position.currentPrice)}"
//            tvProfitLoss.text = "P/L: $${String.format("%.2f", profitLoss)}"
//
//            btnDelete.setOnClickListener {
//                onDeleteClick(position) // ✅ Correctly references PositionEntity
//            }
//        }
//    }
//}

class PositionAdapter(
    private val viewModel: PositionViewModel, // Pass ViewModel to Adapter
    private val onDeleteClick: (PositionEntity) -> Unit
) : RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {

    private var positions = mutableListOf<PositionEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return PositionViewHolder(view, onDeleteClick, viewModel)
    }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        holder.bind(positions[position])
    }

    override fun getItemCount(): Int = positions.size

    fun updateData(newList: List<PositionEntity>) {
        positions = newList.toMutableList()
        notifyDataSetChanged()
    }

    class PositionViewHolder(
        itemView: View,
        private val onDeleteClick: (PositionEntity) -> Unit,
        private val viewModel: PositionViewModel // Pass ViewModel here
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvCoin: TextView = itemView.findViewById(R.id.tvCoin)
        private val tvBuyPrice: TextView = itemView.findViewById(R.id.tvBuyPrice)
        private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCoinCurrent)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvTotalCost: TextView = itemView.findViewById(R.id.tvTotalCost)
        private val tvProfitLoss: TextView = itemView.findViewById(R.id.tvProfitLoss)
        private val btnDelete: ImageView = itemView.findViewById(R.id.closeorder)

        fun bind(position: PositionEntity) {
            val totalCost = position.buyPrice * position.amount

            tvCoin.text = position.coin
            tvBuyPrice.text = "Buy Price: $${position.buyPrice}"
            tvAmount.text = "Amount: ${position.amount} ${position.coin}"
            tvTotalCost.text = "Total: $${String.format("%.2f", totalCost)}"
            tvCurrentPrice.text = "Loading..."  // Placeholder before fetching price

            // Fetch current price dynamically and update UI
            viewModel.fetchCurrentPrice(position) { currentPrice ->
                val profitLoss = (currentPrice * position.amount) - totalCost
                tvCurrentPrice.text = "Current Price: $${String.format("%.2f", currentPrice)}"
                tvProfitLoss.text = "P/L: $${String.format("%.2f", profitLoss)}"
            }

            btnDelete.setOnClickListener {
                onDeleteClick(position)
            }
        }
    }
}

//class dcaCalculator : AppCompatActivity() {
//    private lateinit var viewModel: PositionViewModel
//    private lateinit var adapter: PositionAdapter
//    private lateinit var binding: ActivityDcacalculatorBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDcacalculatorBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        viewModel = ViewModelProvider(this).get(PositionViewModel::class.java)
//
//        val recyclerView = binding.PositionsRecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        adapter = PositionAdapter(viewModel) { position ->
//            viewModel.deletePosition(position)
//        }
//        recyclerView.adapter = adapter
//
//        viewModel.positions.observe(this, { positions ->
//            adapter.updateData(positions)
//        })
//
//        setupSpinner()
//        setupInputListeners()
//        binding.btnAdd.setOnClickListener { addPosition() }
//    }
//
//    private fun setupSpinner() {
//        val coins = listOf("BTC", "ETH", "XRP", "ADA", "DOT")
//        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, coins)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerCoin.adapter = adapter
//    }
//
//    private fun setupInputListeners() {
//        binding.etBuyPrice.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                calculateAmountOrTotal()
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//
//        binding.etAmount.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                calculateTotalCost()
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//    }
//
//    private fun calculateAmountOrTotal() {
//        val buyPrice = binding.etBuyPrice.text.toString().toDoubleOrNull() ?: 0.0
//        val amount = binding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
//        if (buyPrice > 0 && amount > 0) {
//            binding.etTotalCost.setText(String.format("%.2f", buyPrice * amount))
//        }
//    }
//
//    private fun calculateTotalCost() {
//        val buyPrice = binding.etBuyPrice.text.toString().toDoubleOrNull() ?: 0.0
//        val amount = binding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
//        if (buyPrice > 0 && amount > 0) {
//            binding.etTotalCost.setText(String.format("%.2f", buyPrice * amount))
//        }
//    }
//
//    private fun addPosition() {
//        val coin = binding.spinnerCoin.selectedItem.toString()
//        val buyPrice = binding.etBuyPrice.text.toString().toDoubleOrNull() ?: return
//        val amount = binding.etAmount.text.toString().toDoubleOrNull() ?: return
//        val position = PositionEntity(coin = coin, buyPrice = buyPrice, amount = amount)
//        viewModel.insertPosition(position)
//    }
//}
