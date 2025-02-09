package com.example.androidproject.core.usecases.authentication

import com.example.androidproject.core.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveLoggedUserUseCase @Inject constructor(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String): Flow<Boolean> =
        repository.saveLoggedUser(email)
}