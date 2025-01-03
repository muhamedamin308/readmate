package com.example.readmate.ui.settings.fragments

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readmate.databinding.FragmentHelpSupportBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.settings.adapter.HelpAndSupportAdapter
import com.example.readmate.util.helpAndSupportList
import com.example.readmate.util.hideBottomNavigation

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class HelpAndSupportFragment : BaseFragment<FragmentHelpSupportBinding>() {
    private val helpAndSupportAdapter by lazy { HelpAndSupportAdapter() }

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentHelpSupportBinding =
        FragmentHelpSupportBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        helpAndSupportAdapter.submitList(helpAndSupportList)
        setupRecyclerView(
            binding.recyclerExpendable,
            helpAndSupportAdapter,
            LinearLayoutManager.VERTICAL
        )
        navigateBack(binding.navigateBack)
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigation()
    }
}