package com.mati_tech.dca.dcanew.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.FragmentSignupBinding
import com.mati_tech.dca.dcanew.remoteApi.client.ApiClient
import com.mati_tech.dca.dcanew.remoteApi.models.User
import com.mati_tech.dca.dcanew.remoteApi.repos.UserRepos
import com.mati_tech.dca.dcanew.remoteApi.services.UserApiService
import com.mati_tech.dca.dcanew.viewmodel.UserViewModel
import com.mati_tech.dca.dcanew.viewmodel.UserViewModelFactory

class FragmentSignup : Fragment() {

    private lateinit var binding: FragmentSignupBinding
//    private lateinit var userRepository: UserRepos
    private lateinit var viewModel: UserViewModel
//    private lateinit var apiService: UserApiService
//    private val userViewModel: UserViewModel by viewModels {
//        UserViewModelFactory(userRepository)
//    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        val repos = UserRepos(ApiClient.userApiService)
        val factory = UserViewModelFactory(repos)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val progressBar = binding.signupProgressBar

        binding.SignupFragmentLoginAccountText.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentSignup_to_fragmentLogin)

        }

        binding.SignupFragmentCreateAccountBtn.setOnClickListener {
            val fullname = binding.SignupFragmentFullnameEditText.text.toString().trim()
            val email = binding.SignupFragmentEmailEditText.text.toString().trim()
            val password = binding.SignupFragmentPassEditText.text.toString().trim()
            val passwordConfirm = binding.SignupFragmentConfirmPassEditText.text.toString().trim()

            if (fullname.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != passwordConfirm) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(
                id = id,
                full_name = fullname,
                email = email,
                password = password
            )

            // Disable button and show progress bar
            binding.SignupFragmentCreateAccountBtn.isEnabled = false
            progressBar.visibility = View.VISIBLE

            viewModel.registerUser(user)
        }

        viewModel.isUserRegistered.observe(viewLifecycleOwner) { isRegistered ->
            // Re-enable button and hide progress bar
            binding.SignupFragmentCreateAccountBtn.isEnabled = true
            progressBar.visibility = View.GONE

            if (isRegistered == true) {
                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_fragmentSignup_to_fragmentLogin)
            } else {
                Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
//        binding.SignupFragmentCreateAccountBtn.setOnClickListener {
//            val fullname = binding.SignupFragmentFullnameEditText.text.toString().trim()
//            val email = binding.SignupFragmentEmailEditText.text.toString().trim()
//            val password = binding.SignupFragmentPassEditText.text.toString().trim()
//            val passwordConfirm = binding.SignupFragmentConfirmPassEditText.text.toString().trim()
//
//            // Check if both password fields are not empty and match
//            if (password.isNotEmpty() && passwordConfirm.isNotEmpty()) {
//                if (password == passwordConfirm) {
//                    // Check if other fields are not empty
//                    if (fullname.isNotEmpty() && email.isNotEmpty()) {
//                        val user = User(
//                            id = null , // or null depending on your backend
//                            full_name = fullname,
//                            email = email,
//                            password = password
//
//                        )
//                        // Register user through ViewModel
//                        viewModel.registerUser(user)
//                    } else {
//                        Toast.makeText(
//                            requireContext(),
//                            "Please fill in all fields",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    // Passwords don't match
//                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            } else {
//                // Password fields are empty
//                Toast.makeText(
//                    requireContext(),
//                    "Please enter both password fields",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//
//        // Observe result
//        viewModel.isUserRegistered.observe(viewLifecycleOwner, Observer { isRegistered ->
//            if (isRegistered == true) {
//                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT)
//                    .show()
//
//                // Navigate or clear fields
//            } else {
//                Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
//            }
//        })
    }


}
