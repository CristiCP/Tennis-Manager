package com.example.androidproject.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidproject.core.usecases.authentication.GetLoggedUserUseCase
import com.example.androidproject.ui.authentication.AuthActivity
import com.example.androidproject.ui.tennis.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getLoggedUserUseCase: GetLoggedUserUseCase
) : ViewModel() {

    private val _nextScreen = MutableLiveData<Class<*>>()
    val nextScreen: LiveData<Class<*>> get() = _nextScreen

    fun decideNextScreen() {
        _nextScreen.value = if (getLoggedUserUseCase() != null) {
            MainActivity::class.java
        } else {
            AuthActivity::class.java
        }
    }
}