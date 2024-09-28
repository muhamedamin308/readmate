package com.example.readmate.ui.settings.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.databinding.FragmentSettingsBinding
import com.example.readmate.ui.onboarding.activities.IntroActivity
import com.example.readmate.ui.settings.viewmodel.SettingsViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.showMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            logout.setOnClickListener {
                viewModel.signOut()
                Intent(requireActivity(), IntroActivity::class.java).also { intent ->
                    startActivity(intent)
                    requireActivity().finish()
                }
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
                requireContext().showMessage("Not supported yet!")
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.userProfileState.collect {
                when (it) {
                    is AppState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        requireContext().showMessage("Check your Network Connectivity!!")
                    }

                    is AppState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is AppState.Success -> {
                        binding.progressBar.visibility = View.GONE
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