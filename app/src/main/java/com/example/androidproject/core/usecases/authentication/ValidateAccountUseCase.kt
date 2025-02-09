package com.example.androidproject.core.usecases.authentication

import com.example.androidproject.core.AuthenticationRepository
import javax.inject.Inject

class ValidateAccountUseCase @Inject constructor(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String): Boolean =
        repository.validateAccount(email, password)
}