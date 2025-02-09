package com.example.androidproject.core.usecases.authentication

import com.example.androidproject.core.AuthenticationRepository
import javax.inject.Inject

class GetLoggedUserUseCase @Inject constructor(private val repository: AuthenticationRepository) {

    operator fun invoke(): String? = repository.loggedUser
}
