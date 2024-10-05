package com.example.readmate.ui.settings.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.readmate.data.model.firebase.User
import com.example.readmate.databinding.FragmentProfileBinding
import com.example.readmate.ui.settings.viewmodel.EditProfileViewModel
import com.example.readmate.util.AppState
import com.example.readmate.util.showMessage
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModel<EditProfileViewModel>()
    private var userProfileImageUri: Uri? = null
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initImageLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentProfileBinding.inflate(layoutInflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    is AppState.Error -> showError(state.message)
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
                    is AppState.Error -> showError(state.message)
                    is AppState.Loading -> toggleProgressBar(true)
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
            imgEditProfile.setOnClickListener { launchImagePicker() }
            navigateBack.setOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        imageActivityResultLauncher.launch(intent)
    }

    private fun updateUIWithUser(user: User) {
        binding.apply {
            Glide.with(requireContext()).load(user.profileImage).into(imgUserProfile)
            etFirstName.setText(user.name?.split(" ")?.get(0))
            etLastName.setText(user.name?.split(" ")?.getOrNull(1) ?: "")
            etEmail.setText(user.email).also { etEmail.isEnabled = false }
        }
        toggleLoading(false)
    }

    private fun showError(message: String?) {
        toggleLoading(false)
        requireContext().showMessage("Error: ${message ?: "Unknown error"}")
    }

    private fun toggleLoading(isLoading: Boolean) {
        binding.apply {
            signupProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            val visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
            imgUserProfile.visibility = visibility
            etFirstName.visibility = visibility
            etLastName.visibility = visibility
            etEmail.visibility = visibility
            imgEditProfile.visibility = visibility
            btnSaveChanges.visibility = visibility
        }
    }

    private fun toggleProgressBar(show: Boolean) {
        binding.signupProgressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}
