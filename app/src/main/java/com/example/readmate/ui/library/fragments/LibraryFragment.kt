package com.example.readmate.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.model.local.BookState
import com.example.readmate.databinding.FragmentLibraryBinding
import com.example.readmate.ui.base.BaseAdapter
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.library.adapter.BestSellersAdapter
import com.example.readmate.ui.library.adapter.CategoriesAdapter
import com.example.readmate.ui.library.adapter.NewestBooksAdapter
import com.example.readmate.ui.library.adapter.RecommendedBooksAdapter
import com.example.readmate.ui.library.adapter.TopRatedBooksAdapter
import com.example.readmate.ui.library.viewmodel.LibraryViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.showBottomNavigation
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFragment : BaseFragment<FragmentLibraryBinding>() {
    private val viewModel: LibraryViewModel by viewModel()

    private val newestBooksAdapter = NewestBooksAdapter()
    private val recommendedBooksAdapter = RecommendedBooksAdapter(null)
    private val categoriesAdapter = CategoriesAdapter()
    private val bestSellersAdapter = BestSellersAdapter()
    private val topRatedBooksAdapter = TopRatedBooksAdapter()

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentLibraryBinding {
        return FragmentLibraryBinding.inflate(layoutInflater)
    }

    override fun onViewReady() {
        setupRecyclerViews()
        observeAllViewModelData()
        setupAllClickListeners()
    }

    private fun setupRecyclerViews() {
        setupRecyclerView(
            binding.recyclerNewestBooks,
            newestBooksAdapter,
            LinearLayoutManager.HORIZONTAL
        )
        setupRecyclerView(
            binding.recyclerRecommendedBooks,
            recommendedBooksAdapter,
            LinearLayoutManager.HORIZONTAL
        )
        setupRecyclerView(
            binding.recyclerCategories,
            categoriesAdapter,
            LinearLayoutManager.HORIZONTAL
        )
        setupRecyclerView(
            binding.recyclerBestSellersBooks,
            bestSellersAdapter,
            LinearLayoutManager.HORIZONTAL
        )
        setupRecyclerView(
            binding.recyclerTop5Books,
            topRatedBooksAdapter,
            LinearLayoutManager.VERTICAL
        )
    }

    private fun setupAllClickListeners() {
        val onClickAction: (book: Book, state: BookState) -> Unit = { book, state ->
            findNavController().navigate(
                R.id.action_homeFragment_to_firebaseBookDetailsFragment,
                Bundle().apply {
                    putParcelable("book", book)
                    putString("bookState", state.name)
                }
            )
        }

        bestSellersAdapter.onClick = { book -> onClickAction(book, BookState.BEST_SELLER) }
        newestBooksAdapter.onClick = { book -> onClickAction(book, BookState.NEW) }
        recommendedBooksAdapter.onClick = { book -> onClickAction(book, BookState.RECOMMENDED) }
        topRatedBooksAdapter.onClick = { book -> onClickAction(book, BookState.HIGH_RATED) }

        binding.tvRecommendedSeeAll.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_showAllFragment
            )
        }

        binding.icNotification.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_notificationsFragment
            )
        }
    }


    private fun observeAllViewModelData() {
        observeState(viewModel.newestBooks, binding.newestBooksProgressBar, newestBooksAdapter)
        observeState(
            viewModel.recommendedBooks,
            binding.recommendedBooksProgressBar,
            recommendedBooksAdapter
        )
        observeState(viewModel.bookCategories, binding.categoryBooksProgressBar, categoriesAdapter)
        observeState(
            viewModel.bestSellersBooks,
            binding.bestSellersBooksProgressBar,
            bestSellersAdapter
        )
        observeState(viewModel.topRatedBooks, binding.top5BooksProgressBar, topRatedBooksAdapter)
    }

    private fun <T> observeState(
        flow: StateFlow<AppState<List<T>>>,
        progressBar: View,
        adapter: BaseAdapter<T>
    ) {
        lifecycleScope.launchWhenStarted {
            flow.collect { state ->
                when (state) {
                    is AppState.Loading -> viewVisibility(progressBar, true)
                    is AppState.Error -> {
                        viewVisibility(progressBar, false)
                        showMessage(state.message.toString())
                    }

                    is AppState.Success -> {
                        viewVisibility(progressBar, false)
                        adapter.submitList(state.data!!)
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }
}