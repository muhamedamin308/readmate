package com.example.readmate.ui.explore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.R
import com.example.readmate.data.model.responses.BookResponse
import com.example.readmate.data.service.remote.api.ApiResult
import com.example.readmate.databinding.FragmentExploreBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.explore.adapter.ExploreAllBooksAdapter
import com.example.readmate.ui.explore.adapter.ExploreCategoriesAdapter
import com.example.readmate.ui.explore.adapter.RecentBooksAdapter
import com.example.readmate.ui.explore.viewmodel.ExploreViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.Constants.CLICKED_BOOK
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ExploreFragment : BaseFragment<FragmentExploreBinding>() {
    private val viewModel by viewModel<ExploreViewModel>()
    private val recentBooksAdapter by lazy { RecentBooksAdapter() }
    private val topCategoriesAdapter by lazy { ExploreCategoriesAdapter() }
    private val topRecommendedBooksAdapter by lazy { ExploreAllBooksAdapter(null) }

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentExploreBinding =
        FragmentExploreBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setupRecyclerViews()
        observeViewModel()
        setUpClickListener()
    }

    private fun setupRecyclerViews() {
        setupRecyclerView(
            binding.recyclerRecentBooks,
            recentBooksAdapter,
            LinearLayoutManager.HORIZONTAL
        )
        setupRecyclerView(
            binding.recyclerTopCategories,
            topCategoriesAdapter,
            LinearLayoutManager.HORIZONTAL
        )
        setupRecyclerView(
            binding.recyclerRecommendedBooks,
            topRecommendedBooksAdapter,
            LinearLayoutManager.VERTICAL
        )
    }

    private fun setUpClickListener() {
        recentBooksAdapter.onClick = {
            findNavController().navigate(
                R.id.action_exploreFragment2_to_exploreBookDetailsFragment, Bundle().apply {
                    putString(CLICKED_BOOK, it.id.filter { it.isDigit() })
                }
            )
        }
        topCategoriesAdapter.onClick = {
            findNavController().navigate(
                R.id.action_exploreFragment2_to_exploreShowAllFragment, Bundle().apply {
                    putString("category", it)
                }
            )
        }
        topRecommendedBooksAdapter.onClick = {
            findNavController().navigate(
                R.id.action_exploreFragment2_to_exploreBookDetailsFragment, Bundle().apply {
                    putString(CLICKED_BOOK, it.id.filter { it.isDigit() })
                }
            )
        }
        binding.etExploreSearch.setOnClickListener {
            findNavController().navigate(
                R.id.action_exploreFragment2_to_exploreResultsFragment
            )
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiRecentBookState.collect { handleApiRecentBooks(it) }
                }
                launch {
                    viewModel.categoriesState.collect { handleCategoriesState(it) }
                }
            }
        }
    }

    private fun handleApiRecentBooks(state: ApiResult<BookResponse>) {
        when (state) {
            is ApiResult.Loading -> viewVisibility(binding.newestBooksProgressBar, true)
            is ApiResult.Success -> {
                viewVisibility(binding.newestBooksProgressBar, false)
                viewVisibility(binding.recommendedBooksProgressBar, false)
                state.data.books?.let {
                    val shuffledRecentBooks = it.shuffled().take(5)
                    recentBooksAdapter.submitList(shuffledRecentBooks)
                    topRecommendedBooksAdapter.submitList(it)
                }
            }

            is ApiResult.Error -> {
                viewVisibility(binding.newestBooksProgressBar, false)
                showMessage(state.message)
            }
        }
    }

    private fun handleCategoriesState(state: AppState<List<String>>) {
        when (state) {
            is AppState.Loading -> viewVisibility(binding.topAuthorsProgressBar, true)
            is AppState.Success -> {
                viewVisibility(binding.topAuthorsProgressBar, false)
                topCategoriesAdapter.submitList(state.data!!)
            }

            else -> Unit
        }
    }
}
