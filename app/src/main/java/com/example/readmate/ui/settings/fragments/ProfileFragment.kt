package com.example.readmate.ui.settings.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.readmate.R
import com.example.readmate.data.model.firebase.User
import com.example.readmate.databinding.FragmentProfileBinding
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.settings.viewmodel.EditProfileViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.hideBottomNavigation
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel by viewModel<EditProfileViewModel>()
    private var userProfileImageUri: Uri? = null
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initImageLauncher()
    }

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        observeProfileState()
        observeEditProfileState()
        setClickListeners()
    }

    private fun initImageLauncher() {
        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                userProfileImageUri = it.data?.data
                Glide.with(this).load(userProfileImageUri).into(binding.imgUserProfile)
            }
    }

    private fun observeProfileState() {
        lifecycleScope.launchWhenStarted {
            viewModel.profileState.collectLatest { state ->
                when (state) {
                    is AppState.Loading -> toggleLoading(true)
                    is AppState.Success -> updateUIWithUser(state.data!!)
                    is AppState.Error -> {
                        toggleLoading(false)
                        showMessage(state.message)
                    }

                    is AppState.Ideal -> Unit
                }
            }
        }
    }

    private fun observeEditProfileState() {
        lifecycleScope.launchWhenStarted {
            viewModel.editProfile.collectLatest { state ->
                when (state) {
                    is AppState.Success -> findNavController().navigateUp()
                    is AppState.Error -> {
                        toggleLoading(false)
                        showMessage(state.message)
                    }

                    is AppState.Loading -> viewVisibility(binding.signupProgressBar, true)
                    is AppState.Ideal -> Unit
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.apply {
            btnSaveChanges.setOnClickListener {
                val user =
                    User("${etFirstName.text} ${etLastName.text}", etEmail.text.toString().trim())
                viewModel.updateUserProfile(user, userProfileImageUri)
            }
            btnChangeProfileImage.setOnClickListener { launchImagePicker() }
            navigateBack(binding.navigateBack)
        }
    }

    private fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        imageActivityResultLauncher.launch(intent)
    }

    private fun updateUIWithUser(user: User) {
        binding.apply {
            Glide.with(requireContext()).load(user.profileImage).error(R.drawable.not_found)
                .into(imgUserProfile)
            etFirstName.setText(user.name?.split(" ")?.get(0))
            etLastName.setText(user.name?.split(" ")?.getOrNull(1) ?: "")
            etEmail.setText(user.email).also { etEmail.isEnabled = false }
        }
        toggleLoading(false)
    }

    private fun toggleLoading(isLoading: Boolean) {
        binding.apply {
            viewVisibility(binding.signupProgressBar, isLoading)
            val visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
            imgUserProfile.visibility = visibility
            etFirstName.visibility = visibility
            etLastName.visibility = visibility
            etEmail.visibility = visibility
            btnChangeProfileImage.visibility = visibility
            btnSaveChanges.visibility = visibility
        }
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigation()
    }
}
