package com.example.readmate.ui.mybook.fragments

import android.view.LayoutInflater
import android.view.View
import com.example.readmate.databinding.FragmentMyBooksBinding
import com.example.readmate.ui.base.BaseFragment

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class MyBookFragment : BaseFragment<FragmentMyBooksBinding>() {

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentMyBooksBinding =
        FragmentMyBooksBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        binding.apply {
            recyclerMyBooks.visibility = View.GONE
            myBooksProgressBar.visibility = View.GONE
            imgEmptyList.visibility = View.VISIBLE
            tvEmptyList.visibility = View.VISIBLE
        }
    }
}