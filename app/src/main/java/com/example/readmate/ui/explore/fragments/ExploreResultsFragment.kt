package com.example.readmate.ui.explore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.R
import com.example.readmate.data.service.remote.api.ApiResult
import com.example.readmate.databinding.FragmentSearchResultsBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.explore.adapter.ExploreAllBooksAdapter
import com.example.readmate.ui.explore.viewmodel.ExploreViewModel
import com.example.readmate.util.Constants.CLICKED_BOOK
import com.example.readmate.util.extractFetchRequestQuery
import com.example.readmate.util.hideBottomNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ExploreResultsFragment : BaseFragment<FragmentSearchResultsBinding>() {
    private val viewModel by viewModel<ExploreViewModel>()
    private val resultsAdapter by lazy {
        ExploreAllBooksAdapter(binding.containerEmptyList)
    }

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentSearchResultsBinding =
        FragmentSearchResultsBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setupRecyclerView(
            binding.recyclerSearchResults,
            resultsAdapter,
            LinearLayoutManager.VERTICAL
        )
        setupClickListeners()
        collectQueriedBookState()
    }

    private fun setupClickListeners() {
        binding.btnApplSearch.setOnClickListener {
            if (binding.etSearchBooks.text.toString().isNotEmpty()) {
                val queryToSearchFor =
                    binding.etSearchBooks.text.toString().extractFetchRequestQuery()
                viewModel.getQueriedBooks(queryToSearchFor)
            } else {
                showMessage("Please enter a book title!")
            }
        }

        resultsAdapter.onClick = {
            findNavController().navigate(
                R.id.action_exploreResultsFragment_to_exploreBookDetailsFragment, Bundle().apply {
                    putString(CLICKED_BOOK, it.id.filter { it.isDigit() })
                }
            )
        }

        navigateBack(binding.navigateBack)
    }

    private fun collectQueriedBookState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.queriedBookState.collect {
                when (it) {
                    is ApiResult.Error -> {
                        viewVisibility(binding.resultsProgressBar, false)
                        showMessage(it.message)
                    }

                    ApiResult.Loading -> viewVisibility(binding.resultsProgressBar, true)
                    is ApiResult.Success -> {
                        viewVisibility(binding.resultsProgressBar, false)
                        it.data.books?.let { result ->
                            if (result.isEmpty()) {
                                showMessage("No books found!")
                                viewVisibility(binding.containerEmptyList, true)
                            } else {
                                viewVisibility(binding.containerEmptyList, false)
                                resultsAdapter.submitList(result)
                            }
                        } ?: {
                            showMessage("No books found!")
                            viewVisibility(binding.containerEmptyList, true)
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigation()
    }
}