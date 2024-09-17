package com.example.readmate.ui.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.readmate.databinding.FragmentOnboardingMainScreenBinding
import com.example.readmate.ui.onboarding.adapter.ViewPagerAdapter
import com.example.readmate.ui.onboarding.fragment.screens.OnBoardingFirstFragment
import com.example.readmate.ui.onboarding.fragment.screens.OnBoardingSecondFragment
import com.example.readmate.ui.onboarding.fragment.screens.OnBoardingThirdFragment

/**
 * @author Muhamed Amin Hassan on 08,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ViewPagerFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingMainScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingMainScreenBinding.inflate(inflater)
        val fragmentList = arrayListOf(
            OnBoardingFirstFragment(),
            OnBoardingSecondFragment(),
            OnBoardingThirdFragment()
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val indicator = binding.circleIndicator
        indicator.setViewPager(binding.viewPager)
        adapter.registerAdapterDataObserver(indicator.adapterDataObserver)
        return binding.root
    }
}