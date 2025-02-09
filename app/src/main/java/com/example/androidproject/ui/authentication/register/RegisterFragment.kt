package com.example.androidproject.ui.authentication.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.androidproject.R
import com.example.androidproject.databinding.FragmentRegisterBinding
import com.example.androidproject.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(
    FragmentRegisterBinding::inflate
) {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setButtonsOnClickListener()
        addTextWatchers()
    }

    private fun initObservers() {
        binding.run {
            viewModel.registerState.observe(viewLifecycleOwner) { messageId ->
                messageId?.let {
                    Toast.makeText(requireContext(), getString(it), Toast.LENGTH_SHORT).show()
                    if (it == R.string.account_valid)
                        findNavController().navigate(R.id.navigation_register_to_login)
                }
            }
            viewModel.registerButtonState.observe(viewLifecycleOwner) { isEnabled ->
                registerButton.isEnabled = isEnabled
            }
            viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
                registerLoadingSpinner.isVisible = isLoading
                registerLoadingText.isVisible = isLoading
            }
        }
    }

    private fun setButtonsOnClickListener() {
        binding.run {
            registerButton.setOnClickListener {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val confirmPassword = binding.passwordConfirmationEditText.text.toString()
                viewModel.attemptRegister(email, password, confirmPassword)
            }
            backToLoginButton.setOnClickListener {
                findNavController().navigate(R.id.navigation_register_to_login)
            }
        }
    }

    private fun addTextWatchers() {
        binding.run {
            val textWatcher = createTextWatcher {
                viewModel.changeRegisterButtonState(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    passwordConfirmationEditText.text.toString()
                )
            }
            emailEditText.addTextChangedListener(textWatcher)
            passwordEditText.addTextChangedListener(textWatcher)
            passwordConfirmationEditText.addTextChangedListener(textWatcher)
        }
    }
}
