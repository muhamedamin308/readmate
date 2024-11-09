package com.example.readmate.ui.library.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.model.firebase.User
import com.example.readmate.data.model.local.BookState
import com.example.readmate.databinding.FragmentFirebaseBookDetailsBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.dialog.addNewBookReview
import com.example.readmate.ui.library.adapter.BookReviewsAdapter
import com.example.readmate.ui.library.adapter.CategoriesAdapter
import com.example.readmate.ui.library.adapter.RecommendedBooksAdapter
import com.example.readmate.ui.library.viewmodel.FirebaseBookDetailViewModel
import com.example.readmate.ui.settings.viewmodel.SettingsViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.convertToMyBook
import com.example.readmate.util.gone
import com.example.readmate.util.hideBottomNavigation
import com.example.readmate.util.show
import com.example.readmate.util.toReviewedUser
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FirebaseBookDetailsFragment : BaseFragment<FragmentFirebaseBookDetailsBinding>() {
    private val args by navArgs<FirebaseBookDetailsFragmentArgs>()
    private val settingsViewModel by viewModel<SettingsViewModel>()
    private val viewModel by viewModel<FirebaseBookDetailViewModel>()

    private val categoriesAdapter by lazy { CategoriesAdapter() }
    private val reviewsAdapter by lazy { BookReviewsAdapter(binding.tvNoBookReviews) }
    private val similarBooksAdapter by lazy { RecommendedBooksAdapter(binding.tvNoSimilarBooks) }

    private lateinit var book: Book
    private lateinit var bookState: BookState
    private lateinit var currentUser: User

    companion object {
        const val ADD_TO_BOOKCASE_TEXT = "Add to Bookcase"
        const val REMOVE_FROM_BOOKCASE_TEXT = "Remove from Bookcase"
        const val BUY_NOW_TEXT = "Buy Now"
        const val READ_BOOK_TEXT = "Read Book"
    }

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentFirebaseBookDetailsBinding =
        FragmentFirebaseBookDetailsBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = args.book
        bookState =
            BookState.fromString(arguments?.getString("bookState") ?: "other")
    }

    override fun onStart() {
        super.onStart()
        book.bookId?.let { bookId ->
            viewModel.checkIfBookIsMyBooks(bookId)
            viewModel.checkIfBookIsBookcase(bookId)
            viewModel.fetchBookReviews(bookId)
        }
        book.averageRating?.let { viewModel.fetchSimilarBooks(it) }
        hideBottomNavigation()
    }

    override fun onViewReady() {
        observeUserProfile()
        setupRecyclers()
        setupActions()
        observeViewModel()
    }

    private fun setupActions() {
        binding.btnAddToBookcase.setOnClickListener { toggleBookcaseState() }
        binding.btnBuyNow.setOnClickListener { toggleMyBookState() }
        binding.addNewBookReview.setOnClickListener { initiateBookReview() }

        similarBooksAdapter.onClick = { book -> navigateToBookDetails(book) }
        navigateBack(binding.navigateBack)
    }

    private fun toggleBookcaseState() {
        viewModel.isInBookcase.value.let { isInBookcase ->
            binding.btnAddToBookcase.text =
                if (isInBookcase) REMOVE_FROM_BOOKCASE_TEXT else ADD_TO_BOOKCASE_TEXT
            if (isInBookcase) book.bookId?.let { viewModel.removeFromBookcase(it) }
            else viewModel.addToBookcase(book)
        }
    }

    private fun toggleMyBookState() {
        viewModel.isInMyBooks.value.let { isInMyBooks ->
            if (isInMyBooks) {
                book.convertToMyBook(bookState).let {
                    findNavController().navigate(
                        R.id.action_firebaseBookDetailsFragment_to_bookReadingFragment,
                        Bundle().apply {
                            putParcelable("mybook", it)
                        })
                }
            } else {
                navigateToPayment()
            }
        }
    }

    private fun navigateToPayment() {
        val myBook = book.convertToMyBook(bookState)
        findNavController().navigate(R.id.action_firebaseBookDetailsFragment_to_paymentMethodFragment,
            Bundle().apply { putParcelable("myBook", myBook) })
    }

    private fun navigateToBookDetails(book: Book) {
        findNavController().navigate(R.id.action_firebaseBookDetailsFragment_self, Bundle().apply {
            putParcelable("book", book)
            putString("bookState", BookState.OTHER.name)
        })
    }

    private fun initiateBookReview() {
        addNewBookReview(currentUser.toReviewedUser()) { review ->
            book.bookId?.let { bookId ->
                viewModel.addNewBookReview(bookId, review)
                viewModel.fetchBookReviews(bookId)
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { collectState(viewModel.addBookToBookcase) }
                launch { collectState(viewModel.removeBookFromBookcase) }
                launch { observeBookcaseState() }
                launch { observeMyBooksState() }
                launch { observeSimilarBooks() }
                launch { observeBookReviews() }
            }
        }
    }

    private suspend fun observeBookReviews() {
        viewModel.allBookReviews.collect { state ->
            viewVisibility(binding.firebaseBookDetailsProgressBar, state is AppState.Loading)
            when (state) {
                is AppState.Success -> reviewsAdapter.submitList(state.data!!.sortedByDescending { it.timestamp })
                is AppState.Error -> showMessage(state.message)
                else -> {}
            }
        }
    }

    private suspend fun observeBookcaseState() {
        viewModel.isInBookcase.collectLatest { isInBookcase ->
            binding.btnAddToBookcase.text =
                if (isInBookcase) REMOVE_FROM_BOOKCASE_TEXT else ADD_TO_BOOKCASE_TEXT
        }
    }

    private suspend fun observeMyBooksState() {
        viewModel.isInMyBooks.collectLatest { isInMyBooks ->
            binding.btnBuyNow.text = if (isInMyBooks) {
                binding.btnAddToBookcase.gone()
                READ_BOOK_TEXT
            } else {
                binding.btnAddToBookcase.show()
                BUY_NOW_TEXT
            }
        }
    }

    private suspend fun observeSimilarBooks() {
        viewModel.similarBooks.collect { state ->
            when (state) {
                is AppState.Success -> {
                    viewVisibility(binding.firebaseBookDetailsProgressBar, false)
                    similarBooksAdapter.submitList(state.data!!)
                    fillBookDetails()
                }

                is AppState.Error -> {
                    viewVisibility(binding.firebaseBookDetailsProgressBar, false)
                    showMessage(state.message)
                }

                is AppState.Loading -> viewVisibility(binding.firebaseBookDetailsProgressBar, true)
                else -> Unit
            }
        }
    }

    private fun observeUserProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.userProfileState.collectLatest { state ->
                    if (state is AppState.Success) currentUser = state.data!!
                    else if (state is AppState.Error) showMessage(state.message)
                }
            }
        }
    }

    private suspend fun <T> collectState(stateFlow: StateFlow<AppState<T>>) {
        stateFlow.collectLatest { state ->
            viewVisibility(binding.firebaseBookDetailsProgressBar, state is AppState.Loading)
            if (state is AppState.Success) showMessage("Operation completed successfully.")
            else if (state is AppState.Error) showMessage(state.message)
        }
    }

    private fun setupRecyclers() {
        setupRecyclerView(
            binding.recyclerBookCategories, categoriesAdapter, LinearLayoutManager.HORIZONTAL
        )
        setupRecyclerView(binding.recyclerBookReview, reviewsAdapter, LinearLayoutManager.VERTICAL)
        setupRecyclerView(
            binding.recyclerRelatedBooks, similarBooksAdapter, LinearLayoutManager.HORIZONTAL
        )
    }

    @SuppressLint("SetTextI18n")
    private fun fillBookDetails() {
        binding.apply {
            Glide.with(this@FirebaseBookDetailsFragment).load(book.image)
                .error(R.drawable.dummy_book).into(bookImage)
            tvBookTitle.text = book.title
            tvBookAuthors.text = book.author
            tvBookSubtitle.text = book.subTitle
            tvBookOverview.text = book.overview
            tvBookRate.text = book.averageRating.toString()
            tvBookTotalReviewers.text = "${book.numberOfReviewers} reviewer/s"
            tvBookPrice.text = book.price.toString()
            tvBookTotalChapters.text = "${book.chapters?.size} chapters"
            tvDiscountValue.apply {
                visibility = if (bookState == BookState.OTHER) View.GONE else View.VISIBLE
                text = "-${bookState.discount * 100}%"
            }
        }
        book.categories?.let { categoriesAdapter.submitList(it) }
        showButtonsAfterLoading()
    }

    private fun showButtonsAfterLoading() {
        binding.btnBuyNow.show()
        binding.btnAddToBookcase.show()
    }

}
