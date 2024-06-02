package com.example.blinkitadmin.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.blinkitadmin.AdminMainActivity
import com.example.blinkitadmin.R
import com.example.blinkitadmin.viewModels.AuthViewModel
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                viewModel.isCurrentUser.collect{
                    if(it){
                        startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                        requireActivity().finish()
                    }else{
                        findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                    }
                }
            }
        },2000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}