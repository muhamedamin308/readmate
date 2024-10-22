package com.example.readmate.ui.settings.fragments

import android.view.LayoutInflater
import android.view.View
import com.example.readmate.databinding.FragmentBookcaseBinding
import com.example.readmate.ui.base.BaseFragment

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class BookcaseFragment : BaseFragment<FragmentBookcaseBinding>() {

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentBookcaseBinding =
        FragmentBookcaseBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        binding.apply {
            recyclerBookcase.visibility = View.GONE
            bookcaseProgressBar.visibility = View.GONE
            imgEmptyList.visibility = View.VISIBLE
            tvEmptyList.visibility = View.VISIBLE
            navigateBack(binding.navigateBack)
        }
    }
}