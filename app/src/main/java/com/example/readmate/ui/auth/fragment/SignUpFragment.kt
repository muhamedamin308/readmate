package com.example.readmate.ui.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.readmate.R
import com.example.readmate.data.model.firebase.User
import com.example.readmate.databinding.FragmentSignupBinding
import com.example.readmate.ui.auth.viewmodel.AuthViewModel
import com.example.readmate.ui.main.HomeActivity
import com.example.readmate.util.AppState
import com.example.readmate.util.Constants.GOOGLE_SIGN_IN_REQUEST
import com.example.readmate.util.RegisterFieldState
import com.example.readmate.util.RegisterValidation
import com.example.readmate.util.TAG
import com.example.readmate.util.showMessage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
        observeViewModel()
    }

    private fun setUpListeners() {
        binding.apply {
            btnSignUp.setOnClickListener { performSignUp() }
            googleSignUp.setOnClickListener { startGoogleSignUp() }
            tvSignIn.setOnClickListener { navigateToLogin() }
        }
    }

    private fun performSignUp() {
        val user = User(
            uid = UUID.randomUUID().toString(),
            name = binding.etFullName.text.toString(),
            email = binding.etEmail.text.toString(),
            profileImage = null,
            books = emptyList(),
            booksToRead = emptyList()
        )
        val password = binding.etPassword.text.toString().trim()
        viewModel.register(user, password)
    }

    private fun startGoogleSignUp() {
        val intent = viewModel.googleSignIn()
        startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_REQUEST) handleGoogleSignInResult(data)
    }

    private fun handleGoogleSignInResult(data: Intent?) {
        try {
            val account =
                GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)
            account?.idToken?.let { viewModel.authWithGoogle(it) }
            observeGoogleAuthState()
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.registerValidate.collectLatest { validation ->
                handleValidationErrors(validation)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.registerState.collect { state ->
                when (state) {

                    is AppState.Success -> {
                        showLoading(true)
                        navigateToLogin()
                    }

                    is AppState.Error -> {
                        showLoading(false)
                        requireContext().showMessage(state.message!!)
                    }

                    else -> showLoading(false)
                }
            }
        }
    }

    private fun handleValidationErrors(validation: RegisterFieldState) {
        validation.email.takeIf { it is RegisterValidation.Failed }?.let { registerValidation ->
            binding.etEmail.apply {
                requestFocus()
                error = (registerValidation as RegisterValidation.Failed).message
            }
        }

        validation.password.takeIf { it is RegisterValidation.Failed }
            ?.let { registerValidation ->
                binding.etPassword.apply {
                    requestFocus()
                    error = (registerValidation as RegisterValidation.Failed).message
                }
            }
    }

    private fun observeGoogleAuthState() {
        lifecycleScope.launchWhenStarted {
            viewModel.googleAuth.collectLatest { state ->
                when (state) {
                    is AppState.Success -> handleLoginSuccess(state.data)
                    is AppState.Error -> requireContext().showMessage(state.message!!)
                    else -> Unit
                }
            }
        }
    }

    private fun handleLoginSuccess(user: User?) {
        requireContext().showMessage("Sign up Success, Welcome ${user?.name}")
        Intent(requireActivity(), HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.signupProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }
}
