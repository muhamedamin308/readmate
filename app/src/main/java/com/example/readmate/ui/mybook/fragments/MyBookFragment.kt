package com.example.readmate.ui.mybook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.R
import com.example.readmate.databinding.FragmentMyBooksBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.mybook.adapter.MyBooksAdapter
import com.example.readmate.ui.mybook.viewmodel.MyBookViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.showBottomNavigation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class MyBookFragment : BaseFragment<FragmentMyBooksBinding>() {
    private val viewModel by viewModel<MyBookViewModel>()
    private val myBooksAdapter by lazy { MyBooksAdapter(binding.containerEmptyList) }

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentMyBooksBinding =
        FragmentMyBooksBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setupRecyclerView(
            binding.recyclerMyBooks,
            myBooksAdapter,
            LinearLayoutManager.VERTICAL
        )
        observeUserBooks()
        myBooksAdapter.onClick = {
            findNavController().navigate(
                R.id.action_myBookFragment_to_bookReadingFragment,
                Bundle().apply {
                    putParcelable("mybook", it)
                })
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

    private fun observeUserBooks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userBooks.collectLatest {
                    when (it) {
                        is AppState.Error -> {
                            viewVisibility(binding.myBooksProgressBar, false)
                            showMessage(it.message)
                        }

                        is AppState.Ideal -> Unit
                        is AppState.Loading -> viewVisibility(binding.myBooksProgressBar, true)
                        is AppState.Success -> {
                            viewVisibility(binding.myBooksProgressBar, false)
                            myBooksAdapter.submitList(it.data!!)
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchUserBooks()
    }

}
