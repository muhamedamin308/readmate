package com.example.readmate.ui.settings.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Book
import com.example.readmate.data.model.local.BookState
import com.example.readmate.databinding.FragmentBookcaseBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.dialog.customAlertDialog
import com.example.readmate.ui.settings.adapter.BookcaseAdapter
import com.example.readmate.ui.settings.viewmodel.BookcaseViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.hideBottomNavigation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class BookcaseFragment : BaseFragment<FragmentBookcaseBinding>() {
    private val viewModel by viewModel<BookcaseViewModel>()
    private val bookcaseAdapter by lazy { BookcaseAdapter(binding.containerEmptyList) }

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentBookcaseBinding =
        FragmentBookcaseBinding.inflate(layoutInflater)

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewReady() {
        super.onViewReady()
        navigateBack(binding.navigateBack)
        setupRecyclerView(
            binding.recyclerBookcase,
            bookcaseAdapter,
            LinearLayoutManager.VERTICAL
        )
        setupSwipeToDelete()
        observeBookcaseViewModel()
        bookcaseAdapter.onClick = { book -> navigateToBookDetails(book) }
    }

    private fun observeBookcaseViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bookcaseBooks.collectLatest {
                    when (it) {
                        is AppState.Error -> {
                            viewVisibility(binding.bookcaseProgressBar, false)
                            showMessage(it.message)
                        }

                        is AppState.Ideal -> Unit
                        is AppState.Loading -> viewVisibility(binding.bookcaseProgressBar, true)
                        is AppState.Success -> {
                            viewVisibility(binding.bookcaseProgressBar, false)
                            bookcaseAdapter.submitList(it.data!!)
                        }
                    }
                }
            }
        }
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val book = bookcaseAdapter.getItem(position)
                customAlertDialog(
                    title = "Delete Book?",
                    message = "This can't be undone.",
                    positiveTitle = "Delete",
                    onPositiveAction = {
                        viewModel.removeFromBookcase(book.bookId!!)
                    },
                    onNegativeAction = { bookcaseAdapter.notifyItemChanged(position) }
                )
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerBookcase)
    }

    private fun navigateToBookDetails(book: Book) {
        findNavController().navigate(
            R.id.action_bookcaseFragment_to_firebaseBookDetailsFragment,
            Bundle().apply {
                putParcelable("book", book)
                putString("bookState", BookState.OTHER.name)
            })
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchBookcaseBooks()
        hideBottomNavigation()
    }
}