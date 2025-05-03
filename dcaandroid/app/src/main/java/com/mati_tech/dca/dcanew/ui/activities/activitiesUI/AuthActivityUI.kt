package com.mati_tech.dca.dcanew.ui.activities.activitiesUI

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.ActivityAuthUiBinding

class AuthActivityUI : AppCompatActivity() {
    private lateinit var navController : NavController
    private lateinit var binding: ActivityAuthUiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthUiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.FragmentContainerViewAuth) as NavHostFragment
        navController = navHostFragment.navController
        navController.navigate(R.id.fragmentLogin)



//        val destination = intent.getStringExtra("navigate_to")
//        if (destination == "login") {
//            navController.navigate(R.id.fragmentLogin) // Navigate directly to Login
//        }
//        if (destination == "signup") {
//            navController.navigate(R.id.fragmentSignup) // Navigate directly to Login
//        }
//        if (destination == "forgotpass") {
//            navController.navigate(R.id.fragmentForgotPassword) // Navigate directly to Login
//        }

    }
    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.FragmentContainerViewAuth)
        return navController.navigateUp() || super.onSupportNavigateUp() // here means by parent also works if one fails to work.
    }
}