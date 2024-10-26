package com.example.readmate.ui.library.fragments

import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.readmate.data.model.firebase.Review
import com.example.readmate.data.model.firebase.User
import com.example.readmate.databinding.FragmentAddNewReviewBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.library.viewmodel.FirebaseBookDetailViewModel
import com.example.readmate.ui.settings.viewmodel.SettingsViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.toReviewedUser
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 26,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class AddBookReviewFragment : BaseFragment<FragmentAddNewReviewBinding>() {
    private val reviewViewModel by viewModel<FirebaseBookDetailViewModel>()
    private val settingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var currentUser: User
    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentAddNewReviewBinding =
        FragmentAddNewReviewBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        observeUserProfile()
        val bookId = arguments?.getString("book_id")
        binding.btnAddReview.setOnClickListener {
            val reviewText = binding.etNewReview.text.toString()
            val currentTimestamp = System.currentTimeMillis()
            if (reviewText.isEmpty()) {
                showMessage("Please enter a review.")
                return@setOnClickListener
            } else {
                val reviewedUser = currentUser.toReviewedUser()
                val newReview = Review(reviewedUser, reviewText, currentTimestamp)
                bookId?.let {
                    reviewViewModel.addNewBookReview(it, newReview)
                    observeAddingReview()
                } ?: run {
                    showMessage("An error occurred while adding the review.")
                }
            }
        }
    }

    private fun observeUserProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.userProfileState.collectLatest {
                    when (it) {
                        is AppState.Error -> showMessage(it.message)

                        is AppState.Success -> {
                            currentUser = it.data!!
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun observeAddingReview() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                reviewViewModel.newBookReview.collect {
                    if (it) {
                        showMessage("Your review added successfully!")
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }
}