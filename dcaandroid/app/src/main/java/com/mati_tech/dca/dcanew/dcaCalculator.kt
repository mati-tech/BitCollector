package com.mati_tech.dca.dcanew

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.ActivityDcacalculatorBinding
import com.mati_tech.dca.dcanew.db.PositionEntity

import com.mati_tech.dca.dcanew.viewmodel.PositionViewModel

import com.mati_tech.dca.dcanew.repos.PositionRepos
import com.mati_tech.dca.dcanew.ui.PositionAdapter
import com.mati_tech.dca.dcanew.viewmodel.PositionViewModelFactory

class dcaCalculator : AppCompatActivity() {
    private lateinit var viewModel: PositionViewModel
    private lateinit var adapter: PositionAdapter
    private lateinit var binding: ActivityDcacalculatorBinding
    private lateinit var repos: PositionRepos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDcacalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repos = PositionRepos(applicationContext)
        val factory = PositionViewModelFactory(repos)
        val viewModel = ViewModelProvider(this, factory).get(PositionViewModel::class.java)
        val recyclerView = binding.PositionsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PositionAdapter(viewModel) { position ->
            viewModel.closePosition(position.id, repos.getCurrentPriceFromApi(position.coin))
        }
        recyclerView.adapter = adapter

        viewModel.positions.observe(this, { positions ->
            adapter.updateData(positions)
        })

        setupSpinner()
        setupInputListeners()
        binding.btnAddPosition.setOnClickListener { addPosition() }
    }

    private fun setupSpinner() {
        val coins = listOf("BTC", "LTC", "ETH", "XRP", "ADA", "DOT")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, coins)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.coinNameSpinner.adapter = adapter
    }

    private fun setupInputListeners() {



        binding.ValueinUSD.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateamountCoin()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.BuyPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateamountCoin()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun calculateamountCoin() {
        val buyPrice = binding.BuyPrice.text.toString().toDoubleOrNull() ?: 0.0
        val ValueUsd = binding.ValueinUSD.text.toString().toDoubleOrNull() ?: 0.0
        if (buyPrice > 0 && ValueUsd > 0) {
            binding.AmountinCoin.setText(String.format("%.2f", ValueUsd / buyPrice ))
        }
        else{
            binding.AmountinCoin.text.clear()
        }
    }

    private fun addPosition() {
        val coin = binding.coinNameSpinner.selectedItem.toString()
        val buyPrice = binding.BuyPrice.text.toString().toDoubleOrNull() ?: return
        val amount = binding.AmountinCoin.text.toString().toDoubleOrNull() ?: return
        val valueinusd = binding.ValueinUSD.text.toString().toDoubleOrNull() ?: return
        val position = PositionEntity(coin = coin, buyPrice = buyPrice, amount = amount, valueUsd = valueinusd)
        viewModel.addPosition(position)
    }
}