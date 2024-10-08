package com.example.readmate.ui.onboarding.fragment

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.readmate.databinding.FragmentOnboardingMainScreenBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.onboarding.adapter.ViewPagerAdapter
import com.example.readmate.ui.onboarding.fragment.screens.OnBoardingFirstFragment
import com.example.readmate.ui.onboarding.fragment.screens.OnBoardingSecondFragment
import com.example.readmate.ui.onboarding.fragment.screens.OnBoardingThirdFragment

/**
 * @author Muhamed Amin Hassan on 08,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@Suppress("UNCHECKED_CAST")
class ViewPagerFragment : BaseFragment<FragmentOnboardingMainScreenBinding>() {

    override fun inflateBinding(layoutInflater: LayoutInflater):
            FragmentOnboardingMainScreenBinding =
        FragmentOnboardingMainScreenBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        val fragmentList = arrayListOf(
            OnBoardingFirstFragment(),
            OnBoardingSecondFragment(),
            OnBoardingThirdFragment()
        )
        val adapter = ViewPagerAdapter(
            fragmentList as ArrayList<Fragment>,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val indicator = binding.circleIndicator
        indicator.setViewPager(binding.viewPager)
        adapter.registerAdapterDataObserver(indicator.adapterDataObserver)
    }
}