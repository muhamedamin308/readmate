package com.example.readmate.ui.explore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.R
import com.example.readmate.data.service.remote.api.ApiResult
import com.example.readmate.databinding.FragmentExploreShowAllBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.explore.adapter.ExploreAllBooksAdapter
import com.example.readmate.ui.explore.viewmodel.ExploreViewModel
import com.example.readmate.util.Constants.API_BOOK
import com.example.readmate.util.extractFetchRequestQuery
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 16,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ExploreShowAllFragment : BaseFragment<FragmentExploreShowAllBinding>() {
    private val exploreAllBooksAdapter by lazy { ExploreAllBooksAdapter() }
    private val viewModel by viewModel<ExploreViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentExploreShowAllBinding =
        FragmentExploreShowAllBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setupRecycler()
        setupClickListener()
        val category = arguments?.getString("category")
        category?.let {
            binding.textView.text = category
            viewModel.getQueriedBooks(category.extractFetchRequestQuery())
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.queriedBookState.collect {
                when (it) {
                    is ApiResult.Error -> {
                        viewVisibility(binding.showAllProgressBar, false)
                        showMessage("Error Loading the books!")
                    }

                    ApiResult.Loading -> viewVisibility(binding.showAllProgressBar, true)
                    is ApiResult.Success -> {
                        viewVisibility(binding.showAllProgressBar, false)
                        exploreAllBooksAdapter.differ.submitList(it.data.books)
                    }
                }
            }
        }
    }

    private fun setupClickListener() {
        exploreAllBooksAdapter.onClick = {
            findNavController().navigate(
                R.id.action_exploreShowAllFragment_to_exploreBookDetailsFragment, Bundle().apply {
                    putString(API_BOOK, it.id.filter { it.isDigit() })
                }
            )
        }
        binding.navigateBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecycler() {
        binding.recyclerShowAll.apply {
            adapter = exploreAllBooksAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}