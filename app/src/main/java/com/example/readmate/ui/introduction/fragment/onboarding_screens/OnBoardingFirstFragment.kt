package com.example.readmate.ui.introduction.fragment.onboarding_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.readmate.R
import com.example.readmate.databinding.FragmentOnboardingScreen1Binding

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class OnBoardingFirstFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingScreen1Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingScreen1Binding.inflate(inflater)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.tvNextOnboard.setOnClickListener {
            viewPager?.currentItem = 1
        }
        return binding.root
    }
}