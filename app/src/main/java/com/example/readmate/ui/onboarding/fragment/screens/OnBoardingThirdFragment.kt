package com.example.readmate.ui.onboarding.fragment.screens

import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import com.example.readmate.R
import com.example.readmate.databinding.FragmentOnboardingScreen3Binding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.onboarding.viewmodel.OnBoardingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class OnBoardingThirdFragment : BaseFragment<FragmentOnboardingScreen3Binding>() {
    private val viewModel: OnBoardingViewModel by viewModel()
    override fun inflateBinding(layoutInflater: LayoutInflater):
            FragmentOnboardingScreen3Binding =
        FragmentOnboardingScreen3Binding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        binding.btnGetStart.setOnClickListener {
            viewModel.activateGetStarted()
            findNavController().navigate(R.id.action_onBoardingMainFragment_to_signInFragment)
        }
    }

}