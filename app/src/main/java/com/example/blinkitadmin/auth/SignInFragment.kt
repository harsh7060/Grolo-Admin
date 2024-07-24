package com.example.blinkitadmin.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.blinkitadmin.R
import com.example.blinkitadmin.activities.AdminMainActivity
import com.example.blinkitadmin.utils.Utils
import com.example.blinkitadmin.databinding.FragmentSignInBinding
import com.example.blinkitadmin.viewModels.AuthViewModel
import kotlinx.coroutines.launch


class SignInFragment : Fragment() {
    private lateinit var binding : FragmentSignInBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater)

        setStatusBarAndNavigationBarColors()

        onSignUpClick()

        onLoginClick()

        return binding.root
    }

    private fun onSignUpClick() {
        binding.tvSignUp.setOnClickListener{
            findNavController().navigate(R.id.action_signInFragment_to_registerFragment)
        }
    }

    private fun onLoginClick(){
        binding.btnLogin.setOnClickListener{
            Utils.showDialog(requireContext(), "Logging in...")
            val email = binding.etUserEmail.text.toString()
            val password = binding.etUserPassword.text.toString()
            if(email.isEmpty() || password.isEmpty()){
                Utils.showToast(requireContext(), "Please enter valid details❗")
            }else{
                viewModel.signInWithCredentials(email, password, requireContext())
                lifecycleScope.launch {
                    viewModel.isSignedInSuccessfully.collect{
                        if(it){
                            Utils.hideDialog()
                            startActivity(Intent(requireContext(), AdminMainActivity::class.java))
                            requireActivity().finish()
                        }else{
                            Utils.hideDialog()
                        }
                    }
                }
            }
        }
    }

    private fun setStatusBarAndNavigationBarColors() {
        activity?.window?.statusBarColor = resources.getColor(R.color.white)
        activity?.window?.navigationBarColor = resources.getColor(R.color.white)
        val windowInsetsController = ViewCompat.getWindowInsetsController(activity?.window?.decorView!!)
        windowInsetsController?.isAppearanceLightStatusBars = true
        windowInsetsController?.isAppearanceLightNavigationBars = true
    }
}