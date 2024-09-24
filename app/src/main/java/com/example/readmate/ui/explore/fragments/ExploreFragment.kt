package com.example.readmate.ui.explore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.data.model.responses.BookResponse
import com.example.readmate.data.service.remote.api.ApiResult
import com.example.readmate.databinding.FragmentExploreBinding
import com.example.readmate.ui.explore.adapter.ExploreCategoriesAdapter
import com.example.readmate.ui.explore.adapter.ExploreRecommendedBooksAdapter
import com.example.readmate.ui.explore.adapter.RecentBooksAdapter
import com.example.readmate.ui.explore.viewmodel.ExploreViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.showMessage
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private val viewModel by viewModel<ExploreViewModel>()

    private val recentBooksAdapter by lazy { RecentBooksAdapter() }
    private val topCategoriesAdapter by lazy { ExploreCategoriesAdapter() }
    private val topRecommendedBooksAdapter by lazy { ExploreRecommendedBooksAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclers()
        observeViewModel()
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
            is ApiResult.Loading -> setLoadingState(binding.newestBooksProgressBar, true)
            is ApiResult.Success -> {
                setLoadingState(binding.newestBooksProgressBar, false)
                setLoadingState(binding.recommendedBooksProgressBar, false)
                val shuffledRecentBooks = state.data.books.shuffled().take(5)
                recentBooksAdapter.differ.submitList(shuffledRecentBooks)
                topRecommendedBooksAdapter.differ.submitList(state.data.books)
            }

            is ApiResult.Error -> {
                setLoadingState(binding.newestBooksProgressBar, false)
                requireContext().showMessage(state.message)
            }
        }
    }

    private fun handleCategoriesState(state: AppState<List<String>>) {
        when (state) {
            is AppState.Loading -> setLoadingState(binding.topAuthorsProgressBar, true)
            is AppState.Success -> {
                setLoadingState(binding.topAuthorsProgressBar, false)
                topCategoriesAdapter.differ.submitList(state.data)
            }

            else -> Unit
        }
    }

    private fun setLoadingState(progressBar: View, isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUpRecyclers() {
        binding.recyclerRecentBooks.apply {
            adapter = recentBooksAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.recyclerTopCategories.apply {
            adapter = topCategoriesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.recyclerRecommendedBooks.apply {
            adapter = topRecommendedBooksAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}
