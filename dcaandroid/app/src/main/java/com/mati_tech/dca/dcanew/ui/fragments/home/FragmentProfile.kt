package com.mati_tech.dca.dcanew.ui.fragments.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.FragmentProfileBinding


class FragmentProfile : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val balanceTextView = binding.balanceAmount
        val editMoney = binding.editMoney
        val add10Button = binding.btnAdd10
        val add20Button = binding.btnAdd20
        val updateBalanceButton = binding.btnDepositBalance
        val updatewithdrawButton = binding.btnWithdrawBalance

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
                Toast.makeText(requireContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }

        // Withdraw balance
        updatewithdrawButton.setOnClickListener {
            val enteredAmount = editMoney.text.toString()
            if (enteredAmount.isNotEmpty()) {
                val amount = enteredAmount.toFloat()
                withdrawBalance(amount, balanceTextView)
            } else {
                Toast.makeText(requireContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }

        // Edit Profile
        binding.editProfile.setOnClickListener {
            showEditNicknameDialogBox()
        }
    }

    private fun showEditNicknameDialogBox() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit nickname")
        builder.setMessage("Enter your nickname")

        val input = EditText(requireContext())
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
                Toast.makeText(requireContext(), "Invalid nickname", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "Insufficient balance!", Toast.LENGTH_SHORT).show()
        }
    }

}