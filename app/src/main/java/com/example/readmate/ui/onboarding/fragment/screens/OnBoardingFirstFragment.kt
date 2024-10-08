package com.example.readmate.ui.onboarding.fragment.screens

import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.example.readmate.R
import com.example.readmate.databinding.FragmentOnboardingScreen1Binding
import com.example.readmate.ui.base.BaseFragment

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class OnBoardingFirstFragment : BaseFragment<FragmentOnboardingScreen1Binding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater):
            FragmentOnboardingScreen1Binding =
        FragmentOnboardingScreen1Binding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.tvNextOnboard.setOnClickListener {
            viewPager?.currentItem = 1
        }
    }
}