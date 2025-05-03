package com.mati_tech.dca.dcanew.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mati_tech.dca.databinding.ActivityPositionHistoryBinding
import com.mati_tech.dca.dcanew.db.AppDatabase
import com.mati_tech.dca.dcanew.remoteApi.repos.old.PositionRepos
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



}
