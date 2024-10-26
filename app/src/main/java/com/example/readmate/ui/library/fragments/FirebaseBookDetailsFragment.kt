package com.example.readmate.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.databinding.FragmentFirebaseBookDetailsBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.library.adapter.BookReviewsAdapter
import com.example.readmate.ui.library.adapter.CategoriesAdapter
import com.example.readmate.ui.library.adapter.RecommendedBooksAdapter
import com.example.readmate.ui.library.viewmodel.FirebaseBookDetailViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.gone
import com.example.readmate.util.show
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FirebaseBookDetailsFragment : BaseFragment<FragmentFirebaseBookDetailsBinding>() {
    private val args by navArgs<FirebaseBookDetailsFragmentArgs>()
    private val categoriesAdapter by lazy { CategoriesAdapter() }
    private val reviewsAdapter by lazy { BookReviewsAdapter(binding.tvNoBookReviews) }
    private val similarBooksAdapter by lazy { RecommendedBooksAdapter(binding.tvNoSimilarBooks) }
    private val viewModel by viewModel<FirebaseBookDetailViewModel>()
    private lateinit var book: Book

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentFirebaseBookDetailsBinding =
        FragmentFirebaseBookDetailsBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = args.book
    }

    override fun onStart() {
        super.onStart()
        book.bookId?.let { viewModel.checkIfBookIsBookcase(it) }
        book.bookId?.let { viewModel.checkIfBookIsMyBooks(it) }
        book.averageRating?.let { viewModel.fetchSimilarBooks(it) }
        book.bookId?.let { viewModel.fetchBookReviews(it) }
    }

    override fun onViewReady() {
        setupRecyclers()
        handleActions()
        observeViewModel()
    }

    private fun handleActions() {
        binding.btnAddToBookcase.setOnClickListener { toggleBookcaseState(book) }
        binding.addNewBookReview.setOnClickListener {
            findNavController().navigate(
                R.id.action_firebaseBookDetailsFragment_to_addBookReviewFragment, Bundle().apply {
                    putString("book_id", book.bookId)
                }
            )
        }
        navigateBack(binding.navigateBack)
    }

    private fun toggleBookcaseState(book: Book) {
        when (viewModel.isInBookcase.value) {
            true -> {
                binding.btnAddToBookcase.text = "Remove from Bookcase"
                book.bookId?.let { viewModel.removeFromBookcase(it) }
            }

            false -> {
                binding.btnAddToBookcase.text = "Add to Bookcase"
                viewModel.addToBookcase(book)
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { collectBookcaseState(viewModel.addBookToBookcase) }
                launch { collectBookcaseState(viewModel.removeBookFromBookcase) }
                launch { observeBookcaseState() }
                launch { observeMyBooksState() }
                launch { observeSimilarBooks() }
                launch { observeBookReviews() }
            }
        }
    }

    private suspend fun observeBookReviews() {
        viewModel.allBookReviews.collect {
            when (it) {
                is AppState.Loading -> viewVisibility(binding.firebaseBookDetailsProgressBar, true)
                is AppState.Success -> {
                    viewVisibility(binding.firebaseBookDetailsProgressBar, false)
                    val sortedReviews = it.data!!.sortedByDescending { review -> review.timestamp }
                    reviewsAdapter.submitList(sortedReviews)
                }

                is AppState.Error -> {
                    viewVisibility(binding.firebaseBookDetailsProgressBar, false)
                    showMessage(it.message)
                }

                else -> Unit
            }
        }
    }

    private suspend fun observeBookcaseState() {
        viewModel.isInBookcase.collectLatest {
            binding.btnAddToBookcase.text = if (it) "Remove from Bookcase" else "Add to Bookcase"
        }
    }

    private suspend fun observeMyBooksState() {
        viewModel.isInMyBooks.collectLatest {
            if (it) {
                binding.btnBuyNow.text = "Read Book"
                binding.btnAddToBookcase.gone()
            } else {
                binding.btnBuyNow.text = "Buy Now"
                binding.btnAddToBookcase.show()
            }
        }
    }

    private suspend fun observeSimilarBooks() {
        viewModel.similarBooks.collect {
            when (it) {
                is AppState.Loading -> viewVisibility(binding.firebaseBookDetailsProgressBar, true)
                is AppState.Success -> {
                    viewVisibility(binding.firebaseBookDetailsProgressBar, false)
                    similarBooksAdapter.submitList(it.data!!)
                    fillBookDetails(book)
                }

                is AppState.Error -> {
                    viewVisibility(binding.firebaseBookDetailsProgressBar, false)
                    showMessage(it.message)
                }

                else -> Unit
            }
        }
    }

    private suspend fun <T> collectBookcaseState(stateFlow: StateFlow<AppState<T>>) {
        stateFlow.collectLatest {
            when (it) {
                is AppState.Loading -> viewVisibility(binding.firebaseBookDetailsProgressBar, true)
                is AppState.Success -> {
                    viewVisibility(binding.firebaseBookDetailsProgressBar, false)
                    showMessage("Operation completed successfully.")
                }

                is AppState.Error -> {
                    viewVisibility(binding.firebaseBookDetailsProgressBar, false)
                    showMessage(it.message)
                }

                else -> Unit
            }
        }
    }

    private fun setupRecyclers() {
        setupRecyclerView(
            binding.recyclerBookCategories,
            categoriesAdapter,
            LinearLayoutManager.HORIZONTAL
        )
        setupRecyclerView(binding.recyclerBookReview, reviewsAdapter, LinearLayoutManager.VERTICAL)
        setupRecyclerView(
            binding.recyclerRelatedBooks,
            similarBooksAdapter,
            LinearLayoutManager.HORIZONTAL
        )
    }

    private fun fillBookDetails(book: Book) {
        binding.apply {
            Glide.with(requireView()).load(book.image).error(R.drawable.dummy_book).into(bookImage)
            tvBookTitle.text = book.title
            tvBookAuthors.text = book.author
            tvBookSubtitle.text = book.subTitle
            tvBookOverview.text = book.overview
            tvBookRate.text = book.averageRating.toString()
            tvBookTotalReviewers.text = "${book.numberOfReviewers} reviewer/s"
            tvBookPrice.text = book.price.toString()
            tvBookTotalChapters.text = "${book.chapters?.size} chapters"
        }
        book.categories?.let { categoriesAdapter.submitList(it) }
//        book.reviews?.let { reviewsAdapter.submitList(it) }
    }
}


