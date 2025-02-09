package com.example.androidproject.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.androidproject.R
import com.example.androidproject.databinding.FragmentLoginBinding
import com.example.androidproject.ui.BaseFragment
import com.example.androidproject.ui.tennis.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setButtonsOnClickListener()
        addTextWatchers()
    }

    private fun initObservers() {
        binding.run {
            viewModel.loginError.observe(viewLifecycleOwner) { messageId ->
                messageId?.let {
                    Toast.makeText(requireContext(), getString(it), Toast.LENGTH_SHORT).show()
                } ?: startMainActivity()
            }
            viewModel.loginButtonState.observe(viewLifecycleOwner) { isEnabled ->
                loginButton.isEnabled = isEnabled
            }
            viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
                loadingSpinner.isVisible = isLoading
                loadingText.isVisible = isLoading
            }
        }
    }

    private fun setButtonsOnClickListener() {
        binding.run {
            registerButton.setOnClickListener {
                findNavController().navigate(R.id.navigation_login_to_register)
            }
            loginButton.setOnClickListener {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                viewModel.attemptLogin(email, password)
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun addTextWatchers() {
        binding.run {
            val textWatcher = createTextWatcher {
                viewModel.changeLoginButtonState(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            emailEditText.addTextChangedListener(textWatcher)
            passwordEditText.addTextChangedListener(textWatcher)
        }
    }
}
