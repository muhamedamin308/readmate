package com.example.admin.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.admin.databinding.ActivityAddBookBinding
import java.util.UUID

class AddBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val randomBookId = UUID.randomUUID().toString().substring(0, 6)
        binding.etBookId.apply {
            setText("Book id: $randomBookId")
            isEnabled = false
        }
        binding.btnProceedToChapters.setOnClickListener {
            Intent(this@AddBookActivity, AddChapterActivity::class.java).also { intent ->
                intent.putExtra("bookId", randomBookId)
                intent.putExtra("image", binding.etImage.text.toString())
                intent.putExtra("title", binding.etTitle.text.toString())
                intent.putExtra("author", binding.etAuthor.text.toString())
                intent.putExtra("subTitle", binding.etSubTitle.text.toString())
                intent.putExtra("overview", binding.etOverview.text.toString())
                intent.putExtra("yearPublished", binding.etYearPublished.text.toString().toInt())
                intent.putExtra("numberOfPages", binding.etNumberOfPages.text.toString().toInt())
                intent.putExtra("averageRating", binding.etAverageRating.text.toString().toFloat())
                intent.putExtra(
                    "numberOfReviewers",
                    binding.etNumberOfReviewers.text.toString().toInt()
                )
                intent.putExtra("price", binding.etPrice.text.toString().toFloat())
                intent.putExtra("totalChapters", binding.etNumberOfChapters.text.toString().toInt())
                startActivity(intent)
                finish()
            }
        }
    }
}