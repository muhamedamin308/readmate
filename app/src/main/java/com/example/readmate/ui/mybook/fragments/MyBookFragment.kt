package com.example.readmate.ui.mybook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.readmate.databinding.FragmentMyBooksBinding

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class MyBookFragment : Fragment() {
    private lateinit var binding: FragmentMyBooksBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyBooksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerMyBooks.visibility = View.GONE
            myBooksProgressBar.visibility = View.GONE
            imgEmptyList.visibility = View.VISIBLE
            tvEmptyList.visibility = View.VISIBLE
        }
    }
}