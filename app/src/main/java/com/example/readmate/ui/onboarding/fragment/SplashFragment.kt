package com.example.readmate.ui.onboarding.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.readmate.R
import com.example.readmate.databinding.SplashScreenBinding
import com.example.readmate.ui.base.BaseFragment
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
class SplashFragment : BaseFragment<SplashScreenBinding>() {
    private val viewModel: OnBoardingViewModel by viewModel()

    override fun inflateBinding(layoutInflater: LayoutInflater): SplashScreenBinding =
        SplashScreenBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        observeAppState()
    }

    private fun observeAppState() {
        lifecycleScope.launch {
            delay(4000)
            viewModel.appState.collect { state ->
                val navController = findNavController()
                if (navController.currentDestination?.id == R.id.splashFragment) {
                    Log.i("SplashState", state.toString())
                    when (state) {
                        Constants.signInPath -> navController.navigate(state)
                        Constants.HOME_ACTIVITY_ID -> {
                            Intent(requireActivity(), HomeActivity::class.java).also {
                                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(it)
//                                requireActivity().finish()
                            }
                        }

                        else -> navController.navigate(R.id.action_splashFragment_to_onBoardingMainFragment)
                    }
                }
            }
        }
    }
}
