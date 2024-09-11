package com.example.admin.activites

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admin.R
import com.example.admin.databinding.ActivityAddChapterBinding
import com.example.readmate.data.model.firebase.Chapter
import com.example.readmate.data.model.firebase.HeadLine
import com.google.firebase.firestore.FirebaseFirestore

class AddChapterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddChapterBinding
    private var currentChapter = 1
    private var totalChapters = 0
    private val chapterList = arrayListOf<Chapter>()
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
    fun addHeadlineView() {
        val headlineView = layoutInflater.inflate(R.layout.headline_item, null)
        val llHeadlines = findViewById<LinearLayout>(R.id.llHeadlines)
        llHeadlines.addView(headlineView)
    }

    private fun showChapter(chapterNumber: Int) {
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
        val db = FirebaseFirestore.getInstance()
        val bookData = hashMapOf(
            "id" to intent.getStringExtra("bookId"),
            "title" to intent.getStringExtra("title"),
            "image" to intent.getStringExtra("image"),
            "author" to intent.getStringExtra("author"),
            "subTitle" to intent.getStringExtra("subTitle"),
            "overview" to intent.getStringExtra("overview"),
            "yearPublished" to intent.getIntExtra("yearPublished", 0),
            "numberOfPages" to intent.getIntExtra("numberOfPages", 0),
            "averageRating" to intent.getFloatExtra("averageRating", 0f),
            "price" to intent.getFloatExtra("price", 0f),
            "chapters" to chapterList
        )

        // Log the bookData to check its content
        Log.d("AddChapterActivity", "Saving book data: $bookData")

        db.collection("books")
            .add(bookData)
            .addOnSuccessListener {
                Toast.makeText(this, "Book saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { exception ->
                // Log the exception for debugging
                Log.e("AddChapterActivity", "Error saving book", exception)
                Toast.makeText(this, "Error saving book", Toast.LENGTH_SHORT).show()
            }
    }
}