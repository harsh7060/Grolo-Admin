package com.example.blinkitadmin.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.blinkitadmin.R
import com.example.blinkitadmin.databinding.ActivityAdminMainBinding


class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarAndNavigationBarColors()

        NavigationUI.setupWithNavController(binding.bottomMenu, Navigation.findNavController(this,
            R.id.fragmentContainerView2
        ))
    }

    private fun setStatusBarAndNavigationBarColors() {
        window?.statusBarColor = resources.getColor(R.color.yellow)
        window?.navigationBarColor = resources.getColor(R.color.white)
        val windowInsetsController = ViewCompat.getWindowInsetsController(window?.decorView!!)
        windowInsetsController?.isAppearanceLightStatusBars = true
        windowInsetsController?.isAppearanceLightNavigationBars = true
    }
}