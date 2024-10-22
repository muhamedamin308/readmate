package com.example.readmate.ui.library.fragments

import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.databinding.FragmentFirebaseShowAllBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.library.adapter.ShowAllBooksAdapter
import com.example.readmate.ui.library.viewmodel.LibraryViewModel
import com.example.readmate.util.AppState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 08,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class FirebaseShowAllFragment : BaseFragment<FragmentFirebaseShowAllBinding>() {
    private val viewModel by viewModel<LibraryViewModel>()
    private val adapter by lazy { ShowAllBooksAdapter(null) }
    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentFirebaseShowAllBinding =
        FragmentFirebaseShowAllBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setupRecyclerView(
            binding.recyclerShowAll,
            adapter,
            LinearLayoutManager.VERTICAL
        )
        navigateBack(binding.navigateBack)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allBooks.collect {
                    when (it) {
                        is AppState.Error -> {
                            viewVisibility(binding.showAllProgressBar, false)
                        }

                        is AppState.Ideal -> Unit
                        is AppState.Loading -> {
                            viewVisibility(binding.showAllProgressBar, true)
                        }

                        is AppState.Success -> {
                            viewVisibility(binding.showAllProgressBar, false)
                            it.data?.let { books ->
                                adapter.submitList(books)
                            }
                        }
                    }
                }
            }
        }
    }

}