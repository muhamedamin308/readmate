package com.example.readmate.ui.settings.fragments

import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.databinding.FragmentNotificationsBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.settings.adapter.NotificationsAdapter

class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {
    private val notificationsAdapter by lazy { NotificationsAdapter() }
    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentNotificationsBinding =
        FragmentNotificationsBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setUpRecyclerView()
        binding.navigateBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerNotifications.apply {
            adapter = notificationsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}