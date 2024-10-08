package com.example.readmate.ui.onboarding.fragment.screens

import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.example.readmate.R
import com.example.readmate.databinding.FragmentOnboardingScreen2Binding
import com.example.readmate.ui.base.BaseFragment

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class OnBoardingSecondFragment : BaseFragment<FragmentOnboardingScreen2Binding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater):
            FragmentOnboardingScreen2Binding =
        FragmentOnboardingScreen2Binding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.tvNextOnboard.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }
}