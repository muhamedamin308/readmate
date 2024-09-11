package com.example.admin.activites

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.admin.databinding.ActivityActionBinding

class ActionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAddBook.setOnClickListener {
            startActivity(
                Intent(
                    this@ActionActivity,
                    AddBookActivity::class.java
                )
            )
        }
    }
}
