package com.example.readmate.ui.explore.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.responses.BookDetailsResponse
import com.example.readmate.data.service.remote.api.ApiResult
import com.example.readmate.databinding.FragmentExploreBookDetailsBinding
import com.example.readmate.ui.explore.adapter.ExploreSimilarBooksAdapter
import com.example.readmate.ui.explore.viewmodel.ExploreViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.extractSimilarBooksBasedOnName
import com.example.readmate.util.gone
import com.example.readmate.util.show
import com.example.readmate.util.showMessage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URL

class ExploreBookDetailsFragment : Fragment() {
    private lateinit var binding: FragmentExploreBookDetailsBinding
    private val viewModel by viewModel<ExploreViewModel>()
    private val similarBooksAdapter by lazy { ExploreSimilarBooksAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentExploreBookDetailsBinding.inflate(layoutInflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()

        arguments?.getString("apiBook")?.let { bookId ->
            viewModel.getBookDetails(bookId)
        } ?: requireContext().showMessage("Book not found")

        similarBooksAdapter.onClick = { book ->
            findNavController().navigate(
                R.id.action_exploreBookDetailsFragment_self, Bundle().apply {
                    putString("apiBook", book.id.filter { it.isDigit() })
                }
            )
        }

        binding.navigateBack.setOnClickListener {
            findNavController().navigateUp()
        }

        collectBookDetailsState()
        collectQueriedBookState()
    }

    private fun collectBookDetailsState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.bookDetailsState.collect { state ->
                when (state) {
                    is AppState.Loading -> binding.exploreBookDetailsProgressBar.show()
                    is AppState.Success -> {
                        binding.exploreBookDetailsProgressBar.gone()
                        state.data?.let {
                            bindBookDetails(it)
                            viewModel.getQueriedBooks(it.title.extractSimilarBooksBasedOnName())
                        }
                    }

                    is AppState.Error -> showError("This book not found!")
                    else -> Unit
                }
            }
        }
    }

    private fun collectQueriedBookState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.queriedBookState.collect { result ->
                when (result) {
                    is ApiResult.Loading -> binding.exploreSimilarBooksProgressBar.show()
                    is ApiResult.Success -> {
                        binding.exploreSimilarBooksProgressBar.gone()
                        result.let {
                            if (it.data.books.isNullOrEmpty()) {
                                binding.tvNoSimilarBooks.show()
                            } else {
                                binding.tvNoSimilarBooks.gone()
                                similarBooksAdapter.differ.submitList(it.data.books.take(10))
                            }
                        }
                    }

                    is ApiResult.Error -> showError("Unexpected error occurred!")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindBookDetails(data: BookDetailsResponse) = binding.apply {
        Glide.with(requireView()).load(data.image).error(R.drawable.not_found).into(bookImage)
        tvBookTitle.text = data.title
        tvBookAuthors.text = data.authors
        if (data.subtitle.isEmpty()) {
            tvBookSubtitle.gone()
        } else {
            tvBookSubtitle.show()
            tvBookSubtitle.text = data.subtitle
        }
        tvBookPublishYear.text = data.year
        tvBookPagesCount.text = "${data.pages} page"
        tvBookOverview.text = data.description
        tvBookPublisherName.text = data.publisher

        setupExternalLink(btnDownloadBook, data.download)
        setupExternalLink(bookLink, data.url)
    }

    private fun setupExternalLink(view: View, url: String) {
        if (url.isNotEmpty() && URL(url).protocol in listOf("http", "https")) {
            view.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }
    }

    private fun setupRecycler() {
        binding.recyclerRelatedBooks.apply {
            adapter = similarBooksAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun showError(message: String) {
        binding.exploreSimilarBooksProgressBar.gone()
        requireContext().showMessage(message)
    }
}
