package com.example.readmate.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.databinding.FragmentLibraryBinding
import com.example.readmate.ui.base.BaseAdapter
import com.example.readmate.ui.library.adapter.BestSellersAdapter
import com.example.readmate.ui.library.adapter.CategoriesAdapter
import com.example.readmate.ui.library.adapter.NewestBooksAdapter
import com.example.readmate.ui.library.adapter.RecommendedBooksAdapter
import com.example.readmate.ui.library.adapter.TopRatedBooksAdapter
import com.example.readmate.ui.library.viewmodel.LibraryViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.showMessage
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("UNCHECKED_CAST")
class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private val viewModel: LibraryViewModel by viewModel()

    private val adapters: List<BaseAdapter<*>> = listOf(
        NewestBooksAdapter(),
        RecommendedBooksAdapter(),
        CategoriesAdapter(),
        BestSellersAdapter(),
        TopRatedBooksAdapter()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclers()

        collectState(
            viewModel.newestBooks,
            binding.newestBooksProgressBar,
            adapters[0] as BaseAdapter<Book>
        )
        collectState(
            viewModel.recommendedBooks,
            binding.recommendedBooksProgressBar,
            adapters[1] as BaseAdapter<Book>
        )
        collectState(
            viewModel.bookCategories,
            binding.categoryBooksProgressBar,
            adapters[2] as BaseAdapter<String>
        )
        collectState(
            viewModel.bestSellersBooks,
            binding.bestSellersBooksProgressBar,
            adapters[3] as BaseAdapter<Book>
        )
        collectState(
            viewModel.topRatedBooks,
            binding.top5BooksProgressBar,
            adapters[4] as BaseAdapter<Book>
        )
    }

    private fun setUpRecyclers() {
        val recyclerMappings = listOf(
            Pair(binding.recyclerNewestBooks, LinearLayoutManager.HORIZONTAL),
            Pair(binding.recyclerRecommendedBooks, LinearLayoutManager.HORIZONTAL),
            Pair(binding.recyclerCategories, LinearLayoutManager.HORIZONTAL),
            Pair(binding.recyclerBestSellersBooks, LinearLayoutManager.HORIZONTAL),
            Pair(binding.recyclerTop5Books, LinearLayoutManager.VERTICAL)
        )

        recyclerMappings.forEachIndexed { index, (recyclerView, orientation) ->
            recyclerView.apply {
                adapter = adapters[index]
                layoutManager = LinearLayoutManager(requireContext(), orientation, false)
            }
        }
    }

    private fun <T : Any> collectState(
        flow: StateFlow<AppState<List<T>>>,
        progressBar: View,
        adapter: BaseAdapter<T>
    ) {
        lifecycleScope.launchWhenStarted {
            flow.collect { state ->
                when (state) {
                    is AppState.Loading -> progressBarVisibility(progressBar, true)
                    is AppState.Error -> {
                        progressBarVisibility(progressBar, false)
                        requireContext().showMessage(state.message.toString())
                    }

                    is AppState.Success -> {
                        progressBarVisibility(progressBar, false)
                        adapter.differ.submitList(state.data)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun progressBarVisibility(progressBar: View, isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
