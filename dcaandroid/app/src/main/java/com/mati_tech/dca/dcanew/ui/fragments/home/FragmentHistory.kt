package com.mati_tech.dca.dcanew.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mati_tech.dca.databinding.FragmentHistoryBinding
import com.mati_tech.dca.dcanew.db.AppDatabase
import com.mati_tech.dca.dcanew.remoteApi.repos.old.PositionRepos
import com.mati_tech.dca.dcanew.utils.HistoryAdapter
import com.mati_tech.dca.dcanew.viewmodel.HistoryViewModel
import com.mati_tech.dca.dcanew.viewmodel.HistoryViewModelFactory

class FragmentHistory : Fragment() {

    private lateinit var adapter: HistoryAdapter
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getDatabase(requireContext())
        val repository = PositionRepos(db.positionDao())
        val factory = HistoryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(HistoryViewModel::class.java)
        adapter = HistoryAdapter(viewModel){position->
//            showEditPositionDialog(position)

        }
        binding.historyRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecycler.adapter = adapter

        // ✅ Observe LiveData ONCE
        viewModel.historyPositions.observe(requireActivity()) { positions ->
            adapter.updateData(positions) // Always update the adapter when data changes
        }
        viewModel.historyPositionsplus.observe(requireActivity()) { pnl ->
            binding.totalPlusPnl.text = "Total +PNL: $${String.format("%.2f", pnl)}"
             // Always update the adapter when data changes
        }
        viewModel.historyPositionsMinus.observe(requireActivity()) { pnl  ->
            binding.totalMinusPnl.text = "Total -PNL: $${String.format("%.2f", pnl)}"
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
        viewModel.fetchHistoryPlusPNL()
        viewModel.fetchHistoryNegativePNL()
    }
}