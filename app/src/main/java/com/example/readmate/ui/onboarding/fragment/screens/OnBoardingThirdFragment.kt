package com.example.readmate.ui.onboarding.fragment.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.readmate.R
import com.example.readmate.databinding.FragmentOnboardingScreen3Binding
import com.example.readmate.ui.onboarding.viewmodel.OnBoardingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class OnBoardingThirdFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingScreen3Binding
    private val viewModel: OnBoardingViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingScreen3Binding.inflate(inflater)
        binding.btnGetStart.setOnClickListener {
            viewModel.activateGetStarted()
            findNavController().navigate(R.id.action_onBoardingMainFragment_to_signInFragment)
        }
        return binding.root
    }
}