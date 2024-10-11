package com.example.readmate.ui.settings.fragments

import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.databinding.FragmentNotificationsBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.settings.adapter.NotificationsAdapter
import com.example.readmate.ui.settings.viewmodel.NotificationsViewModel
import com.example.readmate.util.AppState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {
    private val notificationsAdapter by lazy { NotificationsAdapter() }
    private val viewModel by viewModel<NotificationsViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentNotificationsBinding =
        FragmentNotificationsBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setUpRecyclerView()
        binding.navigateBack.setOnClickListener {
            findNavController().navigateUp()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.notifications.collect {
                        when (it) {
                            is AppState.Error -> {
                                viewVisibility(binding.notificationsProgressBar, false)
                                showMessage(it.message)
                            }

                            is AppState.Ideal -> Unit
                            is AppState.Loading -> viewVisibility(
                                binding.notificationsProgressBar,
                                true
                            )

                            is AppState.Success -> {
                                viewVisibility(binding.notificationsProgressBar, false)
                                notificationsAdapter.differ.submitList(it.data)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerNotifications.apply {
            adapter = notificationsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}
