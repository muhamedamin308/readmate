package com.example.readmate.ui.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.databinding.FragmentNotificationsBinding
import com.example.readmate.ui.settings.adapter.NotificationsAdapter

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class NotificationsFragment : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding
    private val notificationsAdapter by lazy { NotificationsAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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