package com.example.admin.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.admin.databinding.ActivityDeleteBookBinding
import com.example.admin.util.AppState
import com.example.admin.viewmodel.DeleteBookViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeleteBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteBookBinding
    private val viewModel: DeleteBookViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnDeleteBook.setOnClickListener {
            val bookId = binding.etBookId.text.toString()
            if (bookId.isNotBlank()) {
                viewModel.deleteBook(bookId)
                lifecycleScope.launch {
                    viewModel.deleteBook.collect {
                        when (it) {
                            is AppState.Error -> {
                                Toast.makeText(
                                    this@DeleteBookActivity,
                                    it.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is AppState.Success -> {
                                Toast.makeText(
                                    this@DeleteBookActivity,
                                    it.data,
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            }

                            else -> Unit
                        }
                    }
                }
            } else
                binding.etBookId.error = "Book ID cannot be empty"
        }
    }
}