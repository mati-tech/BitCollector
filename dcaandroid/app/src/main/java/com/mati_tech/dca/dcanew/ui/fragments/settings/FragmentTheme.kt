package com.mati_tech.dca.dcanew.ui.fragments.settings

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.FragmentThemeBinding

class FragmentTheme : Fragment() {

    private lateinit var binding: FragmentThemeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThemeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.SettingThemeDarkThemeRadio.setOnClickListener{
            val sharedPreferences = requireContext().getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
            val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
            setDarkMode(requireContext(), true)
        }

        binding.SettingThemeLightThemeRadio.setOnClickListener{
            val sharedPreferences = requireContext().getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
            val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)

            setDarkMode(requireContext(), false)
        }
        binding.SettingThemeBackArrow.setOnClickListener{
            it.findNavController().navigate(R.id.action_fragmentSetting_to_fragmentTheme)
        }
    }
    fun setDarkMode(context: Context, isDarkMode: Boolean) {
        val sharedPreferences = context.getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isDarkMode", isDarkMode).apply()

        val mode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)

        //restarting the app to see the effect immediately

        val intent = (context as Activity).intent
        context.finish()
        context.startActivity(intent)
    }
}