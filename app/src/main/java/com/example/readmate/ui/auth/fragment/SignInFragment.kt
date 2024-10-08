package com.example.readmate.ui.auth.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.readmate.R
import com.example.readmate.databinding.FragmentLoginBinding
import com.example.readmate.ui.auth.viewmodel.AuthViewModel
import com.example.readmate.ui.base.BaseFragment
import com.example.readmate.ui.main.HomeActivity
import com.example.readmate.util.AppState
import com.example.readmate.util.Constants.GOOGLE_SIGN_IN_REQUEST
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class SignInFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewModel: AuthViewModel by viewModel()

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        setupListeners()
        observeLoginState()
    }

    private fun setupListeners() {
        binding.apply {
            googleSignIn.setOnClickListener { googleSignIn() }
            tvSignup.setOnClickListener { navigateToSignUp() }
            btnSignIn.setOnClickListener { performLogin() }
        }
    }

    private fun navigateToSignUp() {
        findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModel.login(email, password)
        } else {
            showMessage("Please enter both email and password")
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { state ->
                when (state) {
                    is AppState.Loading -> viewVisibility(binding.signInProgressBar, true)
                    is AppState.Error -> {
                        viewVisibility(binding.signInProgressBar, false)
                        showMessage(state.message)
                    }

                    is AppState.Success -> {
                        viewVisibility(binding.signInProgressBar, false)
                        handleLoginSuccess()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun handleLoginSuccess() {
        showMessage("Sign in successfully")
        Intent(requireActivity(), HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    private fun googleSignIn() {
        val intent = viewModel.googleSignIn()
        startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_REQUEST) handleGoogleSignInResult(data)
    }

    private fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (task.isSuccessful) {
            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.authWithGoogle(account.idToken!!)
                observeGoogleAuthState()
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        } else {
            task.exception?.printStackTrace()
        }
    }

    private fun observeGoogleAuthState() {
        lifecycleScope.launchWhenStarted {
            viewModel.googleAuth.collectLatest { state ->
                when (state) {
                    is AppState.Error -> showMessage(state.message!!)
                    is AppState.Success -> handleLoginSuccess()
                    else -> Unit
                }
            }
        }
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
