package com.example.blinkitadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.blinkitadmin.databinding.ActivityAdminMainBinding

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(binding.bottomMenu, Navigation.findNavController(this, R.id.fragmentContainerView2))
    }
}