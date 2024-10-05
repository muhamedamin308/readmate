package com.example.readmate.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.readmate.R
import com.example.readmate.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Safely find NavController for fragmentContainerView2
        val controller = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2)?.findNavController()
        if (controller != null) {
            binding.bottomNavView.setupWithNavController(controller)
        } else {
            Log.e("HomeActivity", "NavController not found for fragmentContainerView2")
        }
    }
}
