package com.mati_tech.dca

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

    private lateinit var etCoin: EditText
    private lateinit var etBuyPrice: EditText
    private lateinit var etAmount: EditText
    private lateinit var btnAddPosition: Button
    private lateinit var tvTotalProfitLoss: TextView
    private lateinit var rvPositions: RecyclerView
//    private lateinit var close: Button


    private lateinit var adapter: PositionAdapter
    private var positions = mutableListOf<PositionModel>()
    private val fileName = "positions.json" // File to store positions data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCoin = findViewById(R.id.etCoin)
        etBuyPrice = findViewById(R.id.etBuyPrice)
        etAmount = findViewById(R.id.etAmount)
        btnAddPosition = findViewById(R.id.btnAddPosition)
        tvTotalProfitLoss = findViewById(R.id.tvTotalProfitLoss)
        rvPositions = findViewById(R.id.rvPositions)
//        close = findViewById(R.id.closeorder)

        // Initialize RecyclerView
        rvPositions.layoutManager = LinearLayoutManager(this)
        adapter = PositionAdapter{ position -> deletePosition(position) }
        rvPositions.adapter = adapter

        // Load saved positions from JSON file
        positions = loadPositions()
        adapter.updateData(positions)

        // Set up button click listener to add new position
        btnAddPosition.setOnClickListener {
            val coin = etCoin.text.toString().trim()
            val buyPrice = etBuyPrice.text.toString().toDoubleOrNull()
            val amount = etAmount.text.toString().toDoubleOrNull()

            if (coin.isNotEmpty() && buyPrice != null && amount != null) {
                val newPosition = PositionModel(
                    id = positions.size + 1, // Generate simple ID
                    coin = coin,
                    buyPrice = buyPrice,
                    amount = amount
                )

                // Add new position to the list
                positions.add(newPosition)

                // Save updated positions to JSON
                savePositions(positions)

                // Update the RecyclerView
                adapter.updateData(positions)

                // Clear input fields
                etCoin.text.clear()
                etBuyPrice.text.clear()
                etAmount.text.clear()

                // Show success message
                Toast.makeText(this, "Position added!", Toast.LENGTH_SHORT).show()

                // Optionally, update total profit/loss (just a placeholder here)
                tvTotalProfitLoss.text = "Total Profit/Loss: $${calculateTotalProfitLoss()}"
            } else {
                Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deletePosition(position: PositionModel) {
        positions.remove(position) // Remove the item from the list
        savePositions(positions) // Save updated list to JSON
        adapter.updateData(positions) // Notify the adapter about the changes
    }


    private fun loadPositions(): MutableList<PositionModel> {
        val file = File(filesDir, fileName)

        if (file.exists()) {
            try {
                val inputStream = FileInputStream(file)
                val reader = InputStreamReader(inputStream)
                val type = object : TypeToken<MutableList<PositionModel>>() {}.type
                val positions: MutableList<PositionModel> = Gson().fromJson(reader, type)
                reader.close()
                return positions
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return mutableListOf()
    }

    private fun savePositions(positions: List<PositionModel>) {
        val file = File(filesDir, fileName)
        try {
            val outputStream = FileOutputStream(file)
            val writer = OutputStreamWriter(outputStream)
            val json = Gson().toJson(positions)
            writer.write(json)
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun calculateTotalProfitLoss(): Double {
        // Placeholder for total profit/loss calculation
        // You can implement your logic here to calculate the overall profit or loss.
        var totalProfitLoss = 0.0
        // For now, we just return 0
        return totalProfitLoss
    }
}
