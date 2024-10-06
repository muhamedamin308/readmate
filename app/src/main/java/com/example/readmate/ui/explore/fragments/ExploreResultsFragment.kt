package com.example.readmate.ui.explore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.R
import com.example.readmate.data.service.remote.api.ApiResult
import com.example.readmate.databinding.FragmentSearchResultsBinding
import com.example.readmate.ui.explore.adapter.ExploreAllBooksAdapter
import com.example.readmate.ui.explore.viewmodel.ExploreViewModel
import com.example.readmate.util.Constants.API_BOOK
import com.example.readmate.util.extractFetchRequestQuery
import com.example.readmate.util.gone
import com.example.readmate.util.show
import com.example.readmate.util.showMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ExploreResultsFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultsBinding
    private val viewModel by viewModel<ExploreViewModel>()
    private val resultsAdapter by lazy { ExploreAllBooksAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSearchResultsBinding.inflate(layoutInflater).also { binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        binding.btnApplSearch.setOnClickListener {
            if (binding.etSearchBooks.text.toString().isNotEmpty()) {
                val queryToSearchFor =
                    binding.etSearchBooks.text.toString().extractFetchRequestQuery()
                viewModel.getQueriedBooks(queryToSearchFor)
            } else {
                requireContext().showMessage("Please enter a book title!")
            }
        }

        resultsAdapter.onClick = {
            findNavController().navigate(
                R.id.action_exploreResultsFragment_to_exploreBookDetailsFragment, Bundle().apply {
                    putString(API_BOOK, it.id.filter { it.isDigit() })
                }
            )
        }

        binding.navigateBack.setOnClickListener {
            findNavController().navigateUp()
        }

        collectQueriedBookState()
    }

    private fun collectQueriedBookState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.queriedBookState.collect {
                when (it) {
                    is ApiResult.Error -> {
                        binding.resultsProgressBar.gone()
                        requireContext().showMessage("Make sure to search for valid books!")
                    }

                    ApiResult.Loading -> binding.resultsProgressBar.show()
                    is ApiResult.Success -> {
                        binding.resultsProgressBar.gone()
                        it.data.books?.let { result ->
                            if (result.isEmpty()) {
                                requireContext().showMessage("No books found!")
                                binding.containerEmptyList.show()
                            } else {
                                binding.containerEmptyList.gone()
                                resultsAdapter.differ.submitList(result)
                            }
                        } ?: {
                            requireContext().showMessage("No books found!")
                            binding.containerEmptyList.show()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecycler() {
        binding.recyclerSearchResults.apply {
            adapter = resultsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}