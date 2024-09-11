package com.example.admin.activites

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.admin.databinding.ActivityAddBookBinding

class AddBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnProceedToChapters.setOnClickListener {
            Intent(this@AddBookActivity, AddChapterActivity::class.java).also { intent ->
                intent.putExtra("bookId", binding.etBookId.text.toString())
                intent.putExtra("image", binding.etImage.text.toString())
                intent.putExtra("title", binding.etTitle.text.toString())
                intent.putExtra("author", binding.etAuthor.text.toString())
                intent.putExtra("subtitle", binding.etSubTitle.text.toString())
                intent.putExtra("overview", binding.etOverview.text.toString())
                intent.putExtra("yearPublished", binding.etYearPublished.text.toString().toInt())
                intent.putExtra("numberOfPages", binding.etNumberOfPages.text.toString().toInt())
                intent.putExtra("averageRating", binding.etAverageRating.text.toString().toFloat())
                intent.putExtra("price", binding.etPrice.text.toString().toFloat())
                intent.putExtra("totalChapters", binding.etNumberOfChapters.text.toString().toInt())
                startActivity(intent)
                finish()
            }
        }
    }
}