package com.mati_tech.dca.dcanew.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.ActivityPositionDetailBinding

class PositionDetail : AppCompatActivity() {
    private lateinit var binding: ActivityPositionDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPositionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

// Get data from intent
        val coinName = intent.getStringExtra("coin_name")
        val totalAmount = intent.getStringExtra("total_amount")
        val avgBuyPrice = intent.getStringExtra("avg_buy_price")
//        val realizedPnl = intent.getStringExtra("realized_pnl")
//        val unrealizedPnl = intent.getStringExtra("unrealized_pnl")
        binding.tvCoinName.text = coinName
        binding.tvTotalAmount.text = "Total Amount: $totalAmount"
        binding.tvAvgBuyPrice.text = "Avg Buy Price: $$avgBuyPrice"

    }
}