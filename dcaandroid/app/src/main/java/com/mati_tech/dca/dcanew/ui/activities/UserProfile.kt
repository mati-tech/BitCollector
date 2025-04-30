package com.mati_tech.dca.dcanew.ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.ActivityUserProfileBinding

class UserProfile : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val balanceTextView = findViewById<TextView>(R.id.balance_amount)
        val editMoney = findViewById<EditText>(R.id.edit_money)
        val add10Button = findViewById<Button>(R.id.btn_add_10)
        val add20Button = findViewById<Button>(R.id.btn_add_20)
        val updateBalanceButton = findViewById<Button>(R.id.btn_Deposit_balance)
        val updatewithdrawButton = findViewById<Button>(R.id.btn_withdraw_balance)

        // Load stored balance and nickname
        val balance = sharedPreferences.getFloat("balance", 0.0f)
        val nickname = sharedPreferences.getString("nickname", "Nickname")
        balanceTextView.text = "$${String.format("%.2f", balance)}"
        binding.userNickname.text = nickname

        // Add $10
        add10Button.setOnClickListener {
            DepositBalance(10f, balanceTextView)
        }

        // Add $20
        add20Button.setOnClickListener {
            DepositBalance(20f, balanceTextView)
        }

        // Update balance from input field
        updateBalanceButton.setOnClickListener {
            val enteredAmount = editMoney.text.toString()
            if (enteredAmount.isNotEmpty()) {
                val amount = enteredAmount.toFloat()
                DepositBalance(amount, balanceTextView)
            } else {
                Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }

        // Withdraw balance
        updatewithdrawButton.setOnClickListener {
            val enteredAmount = editMoney.text.toString()
            if (enteredAmount.isNotEmpty()) {
                val amount = enteredAmount.toFloat()
                withdrawBalance(amount, balanceTextView)
            } else {
                Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }

        // Edit Profile
        binding.editProfile.setOnClickListener {
            showEditNicknameDialogBox()
        }
    }

    private fun showEditNicknameDialogBox() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit nickname")
        builder.setMessage("Enter your nickname")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Confirm") { _, _ ->
            val enteredNickname = input.text.toString()
            if (enteredNickname.isNotEmpty()) {
                with(sharedPreferences.edit()) {
                    putString("nickname", enteredNickname)
                    apply()
                }
                binding.userNickname.text = enteredNickname
            } else {
                Toast.makeText(this, "Invalid nickname", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        builder.show()
    }

    private fun DepositBalance(amount: Float, balanceTextView: TextView) {
        val currentBalance = sharedPreferences.getFloat("balance", 0.0f)
        val newBalance = currentBalance + amount

        // Save updated balance
        with(sharedPreferences.edit()) {
            putFloat("balance", newBalance)
            apply()
        }

        // Format to two decimal places and update UI
        balanceTextView.text = "$${String.format("%.2f", newBalance)}"
        binding.editMoney.text.clear()
    }

    private fun withdrawBalance(amount: Float, balanceTextView: TextView) {
        val currentBalance = sharedPreferences.getFloat("balance", 0.0f)

        if (currentBalance >= amount) {
            val newBalance = currentBalance - amount

            // Save updated balance
            with(sharedPreferences.edit()) {
                putFloat("balance", newBalance)
                apply()
            }

            // Format to two decimal places and update UI
            balanceTextView.text = "$${String.format("%.2f", newBalance)}"
            binding.editMoney.text.clear()
        } else {
            Toast.makeText(this, "Insufficient balance!", Toast.LENGTH_SHORT).show()
        }
    }
}
