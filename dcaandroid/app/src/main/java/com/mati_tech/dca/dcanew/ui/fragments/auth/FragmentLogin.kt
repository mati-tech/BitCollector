package com.mati_tech.dca.dcanew.ui.fragments.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.FragmentLoginBinding
import com.mati_tech.dca.dcanew.remoteApi.client.ApiClient
import com.mati_tech.dca.dcanew.remoteApi.models.UserLoginRequest
import com.mati_tech.dca.dcanew.remoteApi.repos.UserReposRemote
import com.mati_tech.dca.dcanew.ui.activities.activitiesUI.MainActivityUI
import com.mati_tech.dca.dcanew.viewmodel.UserViewModel
import com.mati_tech.dca.dcanew.viewmodel.UserViewModelFactory

class FragmentLogin : Fragment() {
    private lateinit var binding: FragmentLoginBinding


    private lateinit var viewModel: UserViewModel
    private lateinit var sharedPreferences: SharedPreferences

    private var enteredEmail: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false )

        val repos = UserReposRemote(ApiClient.userApiService)
        val factory = UserViewModelFactory(repos)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]


        return binding.root
    }

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)


    // ✅ Check if token already exists
    val token = sharedPreferences.getString("AUTH_TOKEN", null)
    if (!token.isNullOrEmpty()) {
        // User is already logged in, redirect to main activity
        val intent = Intent(requireContext(), MainActivityUI::class.java)
        startActivity(intent)
        requireActivity().finish()
        return  // Prevent further execution
    }

    val progressBar = binding.loginProgressBar

    binding.LoginFragmentRegisterText.setOnClickListener {
        it.findNavController().navigate(R.id.action_fragmentLogin_to_fragmentSignup)
    }

    binding.LoginFragmentForgotPasswordText.setOnClickListener {
        it.findNavController().navigate(R.id.action_fragmentLogin_to_fragmentForgotPassword)
    }

    // Handle login button click
    binding.LoginFragmentLoginButton.setOnClickListener {
        val email = binding.LoginEditTextEmail.text.toString().trim()
        val password = binding.LoginPasswordEdittext.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            enteredEmail = email
            val loginRequest = UserLoginRequest(email, password)

            // ✅ Disable button and show progress
            binding.LoginFragmentLoginButton.isEnabled = false
            progressBar.visibility = View.VISIBLE

            // Trigger login via ViewModel
            viewModel.loginUser(loginRequest)

        } else {
            Toast.makeText(requireContext(), "Please enter both email and password", Toast.LENGTH_SHORT).show()
        }
    }

    // Observe login result
    viewModel.loginToken.observe(viewLifecycleOwner) { token ->
        // ✅ Hide progress and re-enable button
        progressBar.visibility = View.GONE
        binding.LoginFragmentLoginButton.isEnabled = true

        if (token != null) {
            // Save token to SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("email", enteredEmail)
            editor.putString("AUTH_TOKEN", token)
            editor.apply()

            // Proceed to MainActivity
            val intent = Intent(requireContext(), MainActivityUI::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            Toast.makeText(requireContext(), "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
        }
    }

    viewModel.networkError.observe(viewLifecycleOwner, Observer { hasError ->
        if (hasError) {
            progressBar.visibility = View.GONE
            binding.LoginFragmentLoginButton.isEnabled = true
            Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show()
        }
    })
}

}