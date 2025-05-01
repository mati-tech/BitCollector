package com.mati_tech.dca.dcanew.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mati_tech.dca.databinding.ActivityPositionHistoryBinding
import com.mati_tech.dca.dcanew.db.AppDatabase
import com.mati_tech.dca.dcanew.remoteApi.repos.PositionRepos
import com.mati_tech.dca.dcanew.utils.HistoryAdapter
import com.mati_tech.dca.dcanew.viewmodel.HistoryViewModel
import com.mati_tech.dca.dcanew.viewmodel.HistoryViewModelFactory

class PositionHistory : AppCompatActivity() {
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter
    private lateinit var binding: ActivityPositionHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPositionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = AppDatabase.getDatabase(this)

        val repository = PositionRepos(db.positionDao())
        val factory = HistoryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(HistoryViewModel::class.java)

        adapter = HistoryAdapter(viewModel){position->
//            showEditPositionDialog(position)

        }
        binding.historyRecycler.layoutManager = LinearLayoutManager(this)
        binding.historyRecycler.adapter = adapter

        // ✅ Observe LiveData ONCE
        viewModel.historyPositions.observe(this) { positions ->
            adapter.updateData(positions) // Always update the adapter when data changes
        }

        // 🟢 Button Clicks: Filter the already observed data
        binding.allPostions.setOnClickListener {
            adapter.updateData(viewModel.historyPositions.value ?: emptyList())
        }

        binding.pluspnl.setOnClickListener {
            val filteredList = viewModel.historyPositions.value?.filter { it.positionPnl >= 0 } ?: emptyList()
            adapter.updateData(filteredList)
        }

        binding.minusPnl.setOnClickListener {
            val filteredList = viewModel.historyPositions.value?.filter { it.positionPnl < 0 } ?: emptyList()
            adapter.updateData(filteredList)
        }

        // Fetch historical data
        viewModel.fetchHistory()
    }

//    private fun showClosePositionDialog(position: PositionEntity) {
//        val builder = AlertDialog.Builder(this)
//
//        builder.setTitle("Close Position")
//        builder.setMessage("Enter the closing price for ${position.coin}")
//
//        // Create an EditText for user input
//        val input = EditText(this)
//        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
//        builder.setView(input)
//
//        // Set up buttons
//        builder.setPositiveButton("Confirm") { _, _ ->
//            val enteredPrice = input.text.toString().toDoubleOrNull()
//            if (enteredPrice != null) {
////                val pnl = enteredPrice - position.buyPrice
//                val pnl = (enteredPrice * position.amount) - (position.buyPrice * position.amount)
//                // Format values to 2 decimal places
//                val formattedPnl = String.format("%.2f", pnl).toDouble()
//                val formattedEnteredPrice = String.format("%.2f", enteredPrice).toDouble()
//                viewModel.closePosition(position.id,formattedPnl,  formattedEnteredPrice)
//            } else {
//                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show()
//            }
//        }
//        builder.setNegativeButton("Cancel") { dialog, _ ->
//            dialog.dismiss()
//        }
//
//        builder.show()
//    }

}
