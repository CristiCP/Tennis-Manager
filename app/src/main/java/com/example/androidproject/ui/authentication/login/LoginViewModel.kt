package com.example.androidproject.ui.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.R
import com.example.androidproject.core.usecases.authentication.ValidateAccountUseCase
import com.example.androidproject.core.usecases.authentication.SaveLoggedUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateAccountUseCase: ValidateAccountUseCase,
    private val saveLoggedUserUseCase: SaveLoggedUserUseCase
) : ViewModel() {

    private val _loginError = MutableLiveData<Int?>()
    val loginError: MutableLiveData<Int?> get() = _loginError

    private val _loginButtonState = MutableLiveData<Boolean>()
    val loginButtonState: LiveData<Boolean> get() = _loginButtonState

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    fun attemptLogin(email: String, password: String) {
        if (validateAccountUseCase(email, password)) {
            _loadingState.value = true
            updateLoginButtonState(false)
            viewModelScope.launch {
                handleLoginResult(email)
                _loadingState.value = false
            }
        } else {
            _loginError.value = R.string.email_password_invalid
        }
    }

    fun changeLoginButtonState(email: String, password: String) {
        _loginButtonState.value = email.isNotBlank() && password.isNotBlank()
    }

    private suspend fun handleLoginResult(email: String) {
        saveLoggedUserUseCase(email).collect { success ->
            if (success) {
                _loginError.value = null
            } else {
                updateLoginButtonState(true)
                _loginError.value = R.string.auth_error
            }
        }
    }

    private fun updateLoginButtonState(isEnabled: Boolean) {
        _loginButtonState.value = isEnabled
    }
}