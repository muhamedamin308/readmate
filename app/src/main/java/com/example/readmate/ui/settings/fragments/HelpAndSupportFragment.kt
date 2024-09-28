package com.example.readmate.ui.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.databinding.FragmentHelpSupportBinding
import com.example.readmate.ui.settings.adapter.HelpAndSupportAdapter
import com.example.readmate.util.helpAndSupportList

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class HelpAndSupportFragment : Fragment() {
    private lateinit var binding: FragmentHelpSupportBinding
    private val helpAndSupportAdapter by lazy { HelpAndSupportAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpSupportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helpAndSupportAdapter.differ.submitList(helpAndSupportList)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerExpendable.apply {
            adapter = helpAndSupportAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}