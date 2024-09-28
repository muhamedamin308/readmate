package com.example.readmate.ui.onboarding.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.readmate.R
import com.example.readmate.databinding.SplashScreenBinding
import com.example.readmate.ui.main.HomeActivity
import com.example.readmate.ui.onboarding.viewmodel.OnBoardingViewModel
import com.example.readmate.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 04,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class SplashFragment : Fragment() {
    private lateinit var binding: SplashScreenBinding
    private val viewModel: OnBoardingViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(4000)
            viewModel.appState.collect {
                when (it) {
                    Constants.signInPath -> findNavController().navigate(it)
                    Constants.HOME_ACTIVITY_ID -> {
                        Intent(requireActivity(), HomeActivity::class.java).also { intent ->
                            intent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                        Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }

                    else -> findNavController().navigate(R.id.action_splashFragment_to_onBoardingMainFragment)
                }
            }
        }
    }
}