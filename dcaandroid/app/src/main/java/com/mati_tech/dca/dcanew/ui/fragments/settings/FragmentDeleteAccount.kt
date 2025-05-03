package com.mati_tech.dca.dcanew.ui.fragments.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.util.findColumnIndexBySuffix
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.FragmentDeleteAccountBinding
import com.mati_tech.dca.dcanew.ui.activities.activitiesUI.AuthActivityUI


class FragmentDeleteAccount : Fragment() {
    private lateinit var binding: FragmentDeleteAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteAccountBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDeleteAccount.setOnClickListener{
            val intent = Intent(requireContext(), AuthActivityUI::class.java)
            val sharedPreferences = requireContext().getSharedPreferences("UserPrefs",
                Context.MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            editor.remove("AUTH_TOKEN")  // Remove the stored token
            editor.remove("UID")
            editor.remove("email")
            editor.remove("FULLNAME")
            editor.apply()  // Commit the changes
            startActivity(intent)

            requireActivity().finish()
        }
    }

}