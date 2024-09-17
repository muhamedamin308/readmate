package com.example.admin.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.admin.R
import com.example.admin.databinding.ActivityAddChapterBinding
import com.example.admin.model.Book
import com.example.admin.model.Chapter
import com.example.admin.model.HeadLine
import com.example.admin.util.AppState
import com.example.admin.util.extractFetchRequestQuery
import com.example.admin.viewmodel.AddBookViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddChapterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddChapterBinding
    private var currentChapter = 1
    private var totalChapters = 0
    private val chapterList = arrayListOf<Chapter>()
    private val viewModel: AddBookViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChapterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        totalChapters = intent.getIntExtra("totalChapters", 0)
        binding.etChapterTitle.hint = "Enter Chapter $currentChapter Title"
        binding.btnAddHeadline.setOnClickListener {
            addHeadlineView()
        }
        binding.btnNextChapter.setOnClickListener {
            saveChapterData()
            if (currentChapter < totalChapters) {
                currentChapter++
                showChapter(currentChapter)
            } else {
                saveBookData()
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun addHeadlineView() {
        val headlineView = layoutInflater.inflate(R.layout.headline_item, null)
        val llHeadlines = findViewById<LinearLayout>(R.id.llHeadlines)
        llHeadlines.addView(headlineView)
    }

    @SuppressLint("SetTextI18n")
    private fun showChapter(chapterNumber: Int) {
        binding.etChapterTitle.hint = "Enter Chapter $chapterNumber Title"
        binding.btnNextChapter.text =
            if (chapterNumber == totalChapters) "Save Book" else "Next Chapter"
        binding.etChapterTitle.text.clear()
        binding.llHeadlines.removeAllViews()
    }

    private fun saveChapterData() {
        val chapterTitle = binding.etChapterTitle.text.toString()
        val headlines = mutableListOf<HeadLine>()
        for (i in 0 until binding.llHeadlines.childCount) {
            val headlineView = binding.llHeadlines.getChildAt(i)
            val etHeadlineTitle = headlineView.findViewById<EditText>(R.id.etHeadlineTitle)
            val etHeadlineContent = headlineView.findViewById<EditText>(R.id.etHeadlineContent)

            val headlineTitle = etHeadlineTitle.text.toString()
            val headlineContent = etHeadlineContent.text.toString()

            if (headlineTitle.isNotBlank() && headlineContent.isNotBlank()) {
                headlines.add(HeadLine(title = headlineTitle, content = headlineContent))
            }
        }
        val chapter = Chapter(
            title = chapterTitle,
            headlines = headlines
        )
        chapterList.add(chapter)
    }

    private fun saveBookData() {
        Book(
            bookId = intent.getStringExtra("bookId"),
            title = intent.getStringExtra("title"),
            image = intent.getStringExtra("image"),
            author = intent.getStringExtra("author"),
            subTitle = intent.getStringExtra("subTitle"),
            overview = intent.getStringExtra("overview"),
            yearPublished = intent.getIntExtra("yearPublished", 0),
            numberOfPages = intent.getIntExtra("numberOfPages", 0),
            averageRating = intent.getFloatExtra("averageRating", 0f),
            numberOfReviewers = intent.getIntExtra("numberOfReviewers", 0),
            price = intent.getFloatExtra("price", 0f),
            categories = intent.getStringExtra("categories")!!.extractFetchRequestQuery(),
            chapters = chapterList
        ).also {
            viewModel.addBook(it)
        }
        lifecycleScope.launch {
            viewModel.addBookState.collect {
                when (it) {
                    is AppState.Error -> {
                        Toast.makeText(
                            this@AddChapterActivity,
                            "Error adding book: ${it.message}",
                            Toast.LENGTH_SHORT
                        )
                    }

                    is AppState.Success -> {
                        Toast.makeText(
                            this@AddChapterActivity,
                            "Book ${it.data?.title} added successfully!",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }

                    else -> Unit
                }
            }
        }
    }
}