package com.mati_tech.dca.dcanew.ui.fragments.home

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.FragmentSettingBinding
import com.mati_tech.dca.dcanew.ui.activities.activitiesUI.AuthActivityUI

class FragmentSetting : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        // Example: Set a click listener (modify as needed)
        // binding.someButton.setOnClickListener { }

        binding.SettingToTheme.setOnClickListener{
            it.findNavController().navigate(R.id.action_fragmentSetting_to_fragmentTheme)
        }

        binding.SettingToAboutUs.setOnClickListener{
            it.findNavController().navigate(R.id.action_fragmentSetting_to_fragmentAboutUs)
        }
        binding.SettingToTelegramLogo.setOnClickListener{
            //add a qr code scanner for the telegram acccount, and the instagram
            showTelegramQRDialog()
        }
        binding.SettingToTelegramLogo2.setOnClickListener{
            //add a qr code scanner for the telegram acccount, and the instagram
            showTelegramQRDialog()
        }
        binding.SettingToInstaLogo.setOnClickListener{
            //add a qr code scanner for the telegram acccount, and the instagram
            showIGQRDialog()
        }
        binding.SettingToDeleteAccount.setOnClickListener{
            it.findNavController().navigate(R.id.action_fragmentSetting_to_fragmentDeleteAccount)
        }
        binding.SettingFragmentSignoutBtn.setOnClickListener{

            signOut()

        }
        return binding.root
    }

    private fun showIGQRDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialogeiglogo, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<ImageView>(R.id.qrCodeImageViewa).setImageResource(R.drawable.ig)

        dialog.show()
    }

    private fun showTelegramQRDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_telegram_qr, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<ImageView>(R.id.qrCodeImageView).setImageResource(R.drawable.mati_tech_tg)

        dialog.show()
    }
    private fun signOut() {
        // Remove the token from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("AUTH_TOKEN")  // Remove the stored token
        editor.remove("UID")
        editor.remove("email")
        editor.remove("FULLNAME")
        editor.apply()  // Commit the changes

        // Navigate to the login screen
        val intent = Intent(requireContext(), AuthActivityUI::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()  // Close MainActivity to prevent returning back to logged-in screen
    }

}