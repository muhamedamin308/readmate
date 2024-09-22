package com.example.readmate.ui.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.readmate.R
import com.example.readmate.data.model.firebase.User
import com.example.readmate.databinding.FragmentLoginBinding
import com.example.readmate.ui.auth.viewmodel.AuthViewModel
import com.example.readmate.ui.main.HomeActivity
import com.example.readmate.util.AppState
import com.example.readmate.util.Constants.GOOGLE_SIGN_IN_REQUEST
import com.example.readmate.util.showMessage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            requireContext().showMessage("Please enter both email and password")
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { state ->
                when (state) {
                    is AppState.Loading -> showLoading(true)
                    is AppState.Error -> {
                        showLoading(false)
                        requireContext().showMessage(state.message!!)
                    }

                    is AppState.Success -> {
                        showLoading(false)
                        handleLoginSuccess(state.data)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.signInProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleLoginSuccess(user: User?) {
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
                    is AppState.Error -> requireContext().showMessage(state.message!!)
                    is AppState.Success -> handleLoginSuccess(state.data)
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
