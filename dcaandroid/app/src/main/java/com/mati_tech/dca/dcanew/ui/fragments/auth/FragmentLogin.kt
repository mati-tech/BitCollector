package com.mati_tech.dca.dcanew.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.mati_tech.dca.R
import com.mati_tech.dca.databinding.FragmentLoginBinding
import com.mati_tech.dca.dcanew.ui.activities.activitiesUI.MainActivityUI
import kotlinx.coroutines.MainScope

class FragmentLogin : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false )
        binding.LoginFragmentRegisterText.setOnClickListener{
            it.findNavController().navigate(R.id.action_fragmentLogin_to_fragmentSignup)
        }
        binding.LoginFragmentForgotPasswordText.setOnClickListener{
            it.findNavController().navigate(R.id.action_fragmentLogin_to_fragmentForgotPassword)

        }
        binding.LoginFragmentLoginButton.setOnClickListener{
            val intent = Intent( context, MainActivityUI::class.java  )
            startActivity(intent)
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}