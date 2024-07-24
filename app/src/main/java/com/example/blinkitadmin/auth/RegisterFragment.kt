package com.example.blinkitadmin.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.blinkitadmin.R
import com.example.blinkitadmin.activities.AdminMainActivity
import com.example.blinkitadmin.databinding.FragmentRegisterBinding
import com.example.blinkitadmin.model.Admin
import com.example.blinkitadmin.utils.Utils
import com.example.blinkitadmin.viewModels.AuthViewModel
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        setStatusBarAndNavigationBarColors()

        onRegisterBtnClick()

        return binding.root
    }

    private fun onRegisterBtnClick() {
        binding.btnRegister.setOnClickListener{
            val number = binding.etUserNumber.text.toString()
            val email = binding.etUserEmail.text.toString()
            val password = binding.etUserPassword.text.toString()
            val name = binding.etUserName.text.toString()

            Utils.showDialog(requireContext(), "Please Wait...")

            if(email.isEmpty() || password.isEmpty() || number.isEmpty() || name.isEmpty()){
                Utils.showToast(requireContext(), "Please enter a valid details❗")
            }else{
                val admin = Admin(uid = null, adminName = name, adminEmail = email, adminPassword = "", adminPhoneNumber = number)
                viewModel.signUpWithCredentials(email, password, admin, requireContext())
                lifecycleScope.launch {
                    viewModel.isSignedUpSuccessfully.collect{
                        if(it){
                            Utils.showToast(requireContext(), "Admin Registered Successfully✅")
                            Utils.hideDialog()
                            startActivity(Intent(requireContext(), AdminMainActivity::class.java))
                            requireActivity().finish()
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