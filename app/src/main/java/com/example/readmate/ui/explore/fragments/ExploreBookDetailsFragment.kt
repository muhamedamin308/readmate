package com.example.readmate.ui.explore.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.responses.BookDetailsResponse
import com.example.readmate.data.service.remote.api.ApiResult
import com.example.readmate.databinding.FragmentExploreBookDetailsBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.explore.adapter.ExploreSimilarBooksAdapter
import com.example.readmate.ui.explore.viewmodel.ExploreViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.Constants.CLICKED_BOOK
import com.example.readmate.util.extractSimilarBooksBasedOnName
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URL

class ExploreBookDetailsFragment : BaseFragment<FragmentExploreBookDetailsBinding>() {
    private val viewModel by viewModel<ExploreViewModel>()
    private val similarBooksAdapter by lazy { ExploreSimilarBooksAdapter() }

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentExploreBookDetailsBinding =
        FragmentExploreBookDetailsBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setupRecyclerView(
            binding.recyclerRelatedBooks,
            similarBooksAdapter,
            LinearLayoutManager.HORIZONTAL
        )

        arguments?.getString(CLICKED_BOOK)?.let { bookId ->
            viewModel.getBookDetails(bookId)
        } ?: showMessage("Book not found")

        similarBooksAdapter.onClick = { book ->
            findNavController().navigate(
                R.id.action_exploreBookDetailsFragment_self, Bundle().apply {
                    putString(CLICKED_BOOK, book.id.filter { it.isDigit() })
                }
            )
        }

        navigateBack(binding.navigateBack)
        collectBookDetailsState()
        collectQueriedBookState()
    }

    private fun collectBookDetailsState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.bookDetailsState.collect { state ->
                when (state) {
                    is AppState.Loading -> viewVisibility(
                        binding.exploreBookDetailsProgressBar,
                        true
                    )

                    is AppState.Success -> {
                        viewVisibility(binding.exploreBookDetailsProgressBar, false)
                        state.data?.let {
                            bindBookDetails(it)
                            viewModel.getQueriedBooks(it.title.extractSimilarBooksBasedOnName())
                        }
                    }

                    is AppState.Error -> {
                        viewVisibility(binding.exploreBookDetailsProgressBar, false)
                        showMessage(state.message)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun collectQueriedBookState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.queriedBookState.collect { result ->
                when (result) {
                    is ApiResult.Loading -> viewVisibility(
                        binding.exploreSimilarBooksProgressBar,
                        true
                    )

                    is ApiResult.Success -> {
                        viewVisibility(binding.exploreSimilarBooksProgressBar, false)
                        result.data.books?.let { books ->
                            val isEmptyList = books.isEmpty()
                            viewVisibility(binding.tvNoSimilarBooks, isEmptyList)

                            if (!isEmptyList) {
                                similarBooksAdapter.submitList(books.take(10))
                            }
                        }
                    }

                    is ApiResult.Error -> {
                        viewVisibility(binding.exploreSimilarBooksProgressBar, false)
                        showMessage(result.message)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindBookDetails(data: BookDetailsResponse) = binding.apply {
        Glide.with(requireView()).load(data.image).error(R.drawable.dummy_book).into(bookImage)
        tvBookTitle.text = data.title
        tvBookAuthors.text = data.authors
        if (data.subtitle.isEmpty()) {
            viewVisibility(tvBookSubtitle, false)
        } else {
            viewVisibility(tvBookSubtitle, true)
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
}
