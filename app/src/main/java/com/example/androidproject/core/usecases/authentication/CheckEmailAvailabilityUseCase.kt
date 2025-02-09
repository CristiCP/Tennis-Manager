package com.example.androidproject.core.usecases.authentication

import com.example.androidproject.core.AuthenticationRepository
import javax.inject.Inject

class CheckEmailAvailabilityUseCase @Inject constructor(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String): Boolean = repository.checkEmailAvailability(email)
}