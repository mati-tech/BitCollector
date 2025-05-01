package com.mati_tech.dca.dcanew.ui.activities.activitiesUI

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.ActivityMainUiBinding

//class MainActivityUI : AppCompatActivity() {
//    private lateinit var navController : NavController
//    lateinit var binding: ActivityMainUiBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        //        Handling the dark mode from the shared  perferences
////        val sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
////        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
////        val mode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
////        AppCompatDelegate.setDefaultNightMode(mode)
//
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainUiBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//
//        // Get NavController from NavHostFragment
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.FragmentContainerViewMainUI) as NavHostFragment
//        navController = navHostFragment.navController
//
//        // Connect BottomNavigationView with NavController
//        NavigationUI.setupWithNavController(binding.bottomNavigationUI, navController)
//        binding.bottomNavigationUI.setOnItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.fragmentHome -> navController.navigate(R.id.fragmentHome)
//                R.id.fragmentPositions -> navController.navigate(R.id.fragmentPositions)
//                R.id.fragmentProfile -> navController.navigate(R.id.fragmentProfile)
//                R.id.fragmentHistory -> navController.navigate(R.id.fragmentHistory)
//                R.id.fragmentSetting -> navController.navigate(R.id.fragmentSetting)
//            }
//            true
//        }
////        binding.bottomNavigationUI.setOnItemSelectedListener { item ->
////            val destinationId = item.itemId
////            val currentDestination = navController.currentDestination?.id
////
////            if (currentDestination != destinationId) {
////                navController.navigate(
////                    destinationId,
////                    null,
////                    NavOptions.Builder()
////                        .setPopUpTo(navController.graph.startDestinationId, true)
////                        .setLaunchSingleTop(true)
////                        .build()
////                )
////            }
////            true
////        }
////         Update BottomNavigationView selection when fragment changes
////        navController.addOnDestinationChangedListener { _, destination, _ ->
////            binding.bottomNavigationUI.menu.findItem(destination.id)?.isChecked = true
////        }
//
////        navController.addOnDestinationChangedListener { _, destination, _ ->
////            Log.d("NavDebug", "Current destination ID: ${destination.id}")
////            val menu = binding.bottomNavigationUI.menu
////            for (i in 0 until menu.size()) {
////                val item = menu.getItem(i)
////                item.isChecked = item.itemId == destination.id
////                Log.d("NavDebug", "Menu item $i ID: ${item.itemId}, isChecked: ${item.isChecked}")
////            }
////        }
//
//        // Custom back behavior: go Home, then exit
////        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
////            override fun handleOnBackPressed() {
////                val currentDestination = navController.currentDestination?.id
////                if (currentDestination != R.id.fragmentHome) {
////                    navController.navigate(
////                        R.id.fragmentHome,
////                        null,
////                        NavOptions.Builder()
////                            .setPopUpTo(navController.graph.startDestinationId, true)
////                            .setLaunchSingleTop(true)
////                            .build()
////                    )
////                } else {
////                    isEnabled = false
////                    onBackPressedDispatcher.onBackPressed() // exit app
////                }
////            }
////        })
//
//    }
//
////    private fun safeNavigate(destinationId: Int) {
////        if (navController.currentDestination?.id != destinationId) {
////            navController.navigate(
////                destinationId,
////                null,
////                NavOptions.Builder()
////                    .setPopUpTo(navController.graph.startDestinationId, false)
////                    .setLaunchSingleTop(true)
////                    .build()
////            )
////        }
////    }
//
//
//    override fun onSupportNavigateUp(): Boolean {
//        navController = findNavController(R.id.FragmentContainerViewMainUI)
//        return navController.navigateUp() || super.onSupportNavigateUp() // here means by parent also works if one fails to work.
//    }
//
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
//
////    override fun onBackPressed() {
////        val navController = findNavController(R.id.FragmentContainerViewMainUI)
////
////        if (navController.currentDestination?.id != R.id.fragmentHome) {
////            // Navigate to Home
////            navController.navigate(R.id.fragmentHome)
////
////            // Set Home item selected in bottom navigation
////            binding.bottomNavigationUI.menu.findItem(R.id.bottom_nav_home)?.isChecked = true
////
////        } else {
////            // Exit app if already on Home
////            super.onBackPressed()
////        }
////    }
//
//}


class MainActivityUI : AppCompatActivity() {
    private lateinit var navController: NavController
    lateinit var binding: ActivityMainUiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainUiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get NavController from NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.FragmentContainerViewMainUI) as NavHostFragment
        navController = navHostFragment.navController

        // Set up BottomNavigationView with custom navigation to clear back stack
        binding.bottomNavigationUI.setOnItemSelectedListener { item ->
            if (navController.currentDestination?.id != item.itemId) {
                navController.popBackStack(R.id.fragmentHome, inclusive = false)
                navController.navigate(item.itemId)
            }
            true
        }

        // Ensure fragmentHome is the initial destination
        if (savedInstanceState == null) {
            navController.popBackStack(R.id.fragmentHome, inclusive = true)
            navController.navigate(R.id.fragmentHome)
            binding.bottomNavigationUI.selectedItemId = R.id.fragmentHome
        }

        // Update BottomNavigationView selection on destination change
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("NavDebug", "Current destination ID: ${destination.id}")
            binding.bottomNavigationUI.menu.findItem(destination.id)?.isChecked = true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.FragmentContainerViewMainUI)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.FragmentContainerViewMainUI)
        val currentDestinationId = navController.currentDestination?.id
        val primaryDestinations = setOf(
            R.id.fragmentHome,
            R.id.fragmentProfile,
            R.id.fragmentPositions,
            R.id.fragmentHistory,
            R.id.fragmentSetting
        )

        when {
            currentDestinationId == R.id.fragmentHome -> {
                AlertDialog.Builder(this)
                    .setTitle("Exit App")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes") { _, _ -> finish() }
                    .setNegativeButton("No", null)
                    .show()
            }
            currentDestinationId in primaryDestinations -> {
                navController.navigate(
                    R.id.fragmentHome,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.fragmentHome, inclusive = false)
                        .setLaunchSingleTop(true)
                        .build()
                )
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}