package com.example.androidproject.ui.authentication.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.R
import com.example.androidproject.core.usecases.authentication.CheckEmailAvailabilityUseCase
import com.example.androidproject.core.usecases.authentication.SaveAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val checkEmailAvailabilityUseCase: CheckEmailAvailabilityUseCase,
    private val saveAccountUseCase: SaveAccountUseCase
) : ViewModel() {

    private val _registerState = MutableLiveData<Int>()
    val registerState: LiveData<Int> get() = _registerState

    private val _registerButtonState = MutableLiveData<Boolean>()
    val registerButtonState: LiveData<Boolean> get() = _registerButtonState

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    fun attemptRegister(email: String, password: String, confirmPassword: String) {
        _registerState.value = when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.email_invalid
            !isPasswordValid(password) -> R.string.password_invalid
            password != confirmPassword -> R.string.passwords_not_matching
            checkEmailAvailabilityUseCase(email) -> R.string.email_already_used
            else -> {
                _loadingState.value = true
                updateRegisterButtonState(false)
                viewModelScope.launch {
                    handleRegisterResult(email, password)
                    _loadingState.value = false
                }
                null
            }
        }
    }

    fun changeRegisterButtonState(email: String, password: String, confirmPassword: String) {
        _registerButtonState.value =
            email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
    }

    private suspend fun handleRegisterResult(email: String, password: String) {
        saveAccountUseCase(email, password).collect { success ->
            _registerState.value =
                if (success) R.string.account_valid
                else {
                    updateRegisterButtonState(true)
                    R.string.error_register_user
                }
        }
    }

    private fun updateRegisterButtonState(isEnabled: Boolean) {
        _registerButtonState.value = isEnabled
    }

    private fun isPasswordValid(password: String): Boolean =
        password.matches(Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%^&*()-+=]).{8,}$"))
}