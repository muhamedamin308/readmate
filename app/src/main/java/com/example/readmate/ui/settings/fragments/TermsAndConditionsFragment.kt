package com.example.readmate.ui.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.readmate.databinding.FragmentTermsConditionsBinding

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class TermsAndConditionsFragment : Fragment() {
    private lateinit var binding: FragmentTermsConditionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTermsConditionsBinding.inflate(inflater, container, false)
        binding.navigateBack.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }
}