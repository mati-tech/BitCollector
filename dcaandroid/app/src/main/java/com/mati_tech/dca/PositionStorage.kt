package com.mati_tech.dca

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.io.File
import java.io.FileReader

class PositionStorage(private val context: Context) {

    private val FILE_NAME = "positions.json"
    private val gson = Gson()


    // Append a new position to the JSON file
    fun addPosition(position: PositionModel) {
        val file = File(context.filesDir, FILE_NAME)
        val existingPositions = if (file.exists()) {
            val type = object : TypeToken<List<PositionModel>>() {}.type
            gson.fromJson<List<PositionModel>>(file.readText(), type) ?: emptyList()
        } else {
            emptyList()
        }

        // Generate a new ID (auto-increment)
        val newId = if (existingPositions.isNotEmpty()) {
            existingPositions.maxOf { it.id } + 1
        } else {
            1
        }

        val newPosition = position.copy(id = newId)
        val updatedList = existingPositions + newPosition

        file.writeText(gson.toJson(updatedList)) // Save updated list
    }

    // Load all positions from JSON file
    fun loadPositions(): List<PositionModel> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return emptyList()

        val type = object : TypeToken<List<PositionModel>>() {}.type
        return gson.fromJson(file.readText(), type) ?: emptyList()
    }
}