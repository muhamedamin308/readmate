package com.example.admin.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admin.databinding.ActivityDeleteBookBinding
import com.google.firebase.firestore.FirebaseFirestore

class DeleteBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteBookBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnDeleteBook.setOnClickListener {
            val bookId = binding.etBookId.text.toString()
            if (bookId.isNotBlank())
                deleteBook(bookId)
            else
                binding.etBookId.error = "Book ID cannot be empty"
        }
    }

    private fun deleteBook(bookId: String) {
        val store = FirebaseFirestore.getInstance()
        store.collection("books")
            .whereEqualTo("bookId", bookId)
            .get()
            .addOnSuccessListener {
                if (it.documents.isEmpty()) {
                    binding.etBookId.error = "Book not found"
                } else {
                    it.documents[0].reference.delete()
                    binding.etBookId.text?.clear()
                    Toast.makeText(
                        this@DeleteBookActivity,
                        "Book $bookId deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}