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
import com.example.readmate.databinding.FragmentExploreShowAllBinding
import com.example.readmate.ui.explore.adapter.ExploreAllBooksAdapter
import com.example.readmate.ui.explore.viewmodel.ExploreViewModel
import com.example.readmate.util.Constants.API_BOOK
import com.example.readmate.util.extractFetchRequestQuery
import com.example.readmate.util.gone
import com.example.readmate.util.show
import com.example.readmate.util.showMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 16,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ExploreShowAllFragment : Fragment() {
    private lateinit var binding: FragmentExploreShowAllBinding
    private val exploreAllBooksAdapter by lazy { ExploreAllBooksAdapter() }
    private val viewModel by viewModel<ExploreViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentExploreShowAllBinding.inflate(layoutInflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()

        val category = arguments?.getString("category")
        category?.let {
            binding.textView.text = category
            viewModel.getQueriedBooks(category.extractFetchRequestQuery())
        }

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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.queriedBookState.collect {
                when (it) {
                    is ApiResult.Error -> {
                        binding.showAllProgressBar.gone()
                        requireContext().showMessage("Error Loading the books!")
                    }

                    ApiResult.Loading -> binding.showAllProgressBar.show()
                    is ApiResult.Success -> {
                        binding.showAllProgressBar.gone()
                        exploreAllBooksAdapter.differ.submitList(it.data.books)
                    }
                }
            }
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