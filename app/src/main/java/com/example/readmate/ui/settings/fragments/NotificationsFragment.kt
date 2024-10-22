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
    private val notificationsAdapter by lazy { NotificationsAdapter(binding.containerEmptyList) }
    private val viewModel by viewModel<NotificationsViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentNotificationsBinding =
        FragmentNotificationsBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setupRecyclerView(
            binding.recyclerNotifications,
            notificationsAdapter,
            LinearLayoutManager.VERTICAL
        )
        navigateBack(binding.navigateBack)
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
                                notificationsAdapter.submitList(it.data!!)
                            }
                        }
                    }
                }
            }
        }
    }
}
