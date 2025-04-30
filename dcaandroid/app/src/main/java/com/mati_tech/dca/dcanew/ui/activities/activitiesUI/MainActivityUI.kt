package com.mati_tech.dca.dcanew.ui.activities.activitiesUI

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.ActivityMainUiBinding

class MainActivityUI : AppCompatActivity() {
    private lateinit var navController : NavController
    lateinit var binding: ActivityMainUiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        //        Handling the dark mode from the shared  perferences
//        val sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
//        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
//        val mode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
//        AppCompatDelegate.setDefaultNightMode(mode)

        super.onCreate(savedInstanceState)
        binding = ActivityMainUiBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Get NavController from NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.FragmentContainerViewMainUI) as NavHostFragment
        navController = navHostFragment.navController

        // Connect BottomNavigationView with NavController
        NavigationUI.setupWithNavController(binding.bottomNavigationUI, navController)
        binding.bottomNavigationUI.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_nav_home -> navController.navigate(R.id.fragmentHome)
                R.id.bottom_nav_positions -> navController.navigate(R.id.fragmentPositions)
                R.id.bottom_nav_profile -> navController.navigate(R.id.fragmentProfile)
                R.id.bottom_nav_history -> navController.navigate(R.id.fragmentHistory)
                R.id.bottom_nav_setting -> navController.navigate(R.id.fragmentSetting)
            }
            true
        }
        // Update BottomNavigationView selection when fragment changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationUI.menu.findItem(destination.id)?.isChecked = true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.FragmentContainerViewMainUI)
        return navController.navigateUp() || super.onSupportNavigateUp() // here means by parent also works if one fails to work.
    }

//    override fun onBackPressed() {
//        val navController = findNavController(R.id.FragmentContainerViewMainUI)
//
//        // Check if current destination is not Home
//        if (navController.currentDestination?.id != R.id.fragmentHome) {
//            // Navigate to Home instead of exiting
//            navController.navigate(R.id.fragmentHome)
//            navController.addOnDestinationChangedListener { _, destination, _ ->
//                binding.bottomNavigationUI.menu.findItem(destination.id)?.isChecked = true
//            }
//
//        } else {
//            // If already on Home, exit as usual
//            super.onBackPressed()
//        }
//    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.FragmentContainerViewMainUI)

        if (navController.currentDestination?.id != R.id.fragmentHome) {
            // Navigate to Home
            navController.navigate(R.id.fragmentHome)

            // Set Home item selected in bottom navigation
            binding.bottomNavigationUI.menu.findItem(R.id.bottom_nav_home)?.isChecked = true

        } else {
            // Exit app if already on Home
            super.onBackPressed()
        }
    }

}