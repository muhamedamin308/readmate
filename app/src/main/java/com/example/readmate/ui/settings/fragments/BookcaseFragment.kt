package com.example.readmate.ui.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.readmate.databinding.FragmentBookcaseBinding

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class BookcaseFragment : Fragment() {
    private lateinit var binding: FragmentBookcaseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookcaseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerBookcase.visibility = View.GONE
            bookcaseProgressBar.visibility = View.GONE
            imgEmptyList.visibility = View.VISIBLE
            tvEmptyList.visibility = View.VISIBLE
        }
    }
}