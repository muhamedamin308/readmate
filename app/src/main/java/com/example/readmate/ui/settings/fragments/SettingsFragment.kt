package com.example.readmate.ui.settings.fragments

import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.databinding.FragmentSettingsBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.onboarding.activities.IntroActivity
import com.example.readmate.ui.settings.viewmodel.SettingsViewModel
import com.example.readmate.util.AppState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    private val viewModel by viewModel<SettingsViewModel>()

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setUpClickListeners()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.userProfileState.collectLatest {
                        when (it) {
                            is AppState.Error -> {
                                viewVisibility(binding.progressBar, false)
                                showMessage(it.message)
                            }

                            is AppState.Loading -> viewVisibility(binding.progressBar, true)
                            is AppState.Success -> {
                                viewVisibility(binding.progressBar, false)
                                binding.apply {
                                    with(it.data!!) {
                                        Glide.with(requireView())
                                            .load(profileImage)
                                            .error(R.drawable.not_found)
                                            .into(imgUserImage)
                                        tvUsername.text = name
                                        tvUseremail.text = email
                                    }
                                }
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun setUpClickListeners() {
        binding.apply {
            logout.setOnClickListener {
                viewModel.signOut()
                Intent(requireActivity(), IntroActivity::class.java).also { intent ->
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
            userinfo.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment2_to_profileFragment)
            }
            btnBookcase.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment2_to_bookcaseFragment)
            }
            btnNotifications.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment2_to_notificationsFragment)
            }
            btnTermsAndConditions.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment2_to_termsAndConditionsFragment)
            }
            btnHelpAndSupport.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment2_to_helpAndSupportFragment)
            }
            btnDarkTheme.setOnClickListener {
                showMessage("Not supported yet!")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchUserProfile()
    }
}