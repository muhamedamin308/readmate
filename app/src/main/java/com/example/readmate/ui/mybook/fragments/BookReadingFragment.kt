package com.example.readmate.ui.mybook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.data.model.firebase.MyBook
import com.example.readmate.databinding.FragmentReadingBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.mybook.adapter.BookChaptersAdapter
import com.example.readmate.util.hideBottomNavigation

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class BookReadingFragment : BaseFragment<FragmentReadingBinding>() {
    private val args by navArgs<BookReadingFragmentArgs>()
    private val chaptersAdapter by lazy { BookChaptersAdapter() }
    private lateinit var book: MyBook

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentReadingBinding =
        FragmentReadingBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = args.mybook
    }

    override fun onViewReady() {
        super.onViewReady()
        setupRecyclerView(
            binding.recyclerBookChapters,
            chaptersAdapter,
            LinearLayoutManager.VERTICAL
        )
        navigateBack(binding.navigateBack)
        chaptersAdapter.submitList(book.chapters!!)
        binding.textView.text = book.title
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigation()
    }
}