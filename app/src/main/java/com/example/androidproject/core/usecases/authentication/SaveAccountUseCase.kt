package com.example.androidproject.core.usecases.authentication

import com.example.androidproject.core.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveAccountUseCase @Inject constructor(private val repository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String): Flow<Boolean> =
        repository.saveAccount(email, password)
}