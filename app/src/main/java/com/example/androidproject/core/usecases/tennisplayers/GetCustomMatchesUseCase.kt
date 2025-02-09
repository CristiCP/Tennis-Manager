package com.example.androidproject.core.usecases.tennisplayers

import com.example.androidproject.core.TennisPlayersRepository
import com.example.androidproject.core.domain.CustomMatch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCustomMatchesUseCase @Inject constructor(private val repository: TennisPlayersRepository) {

    operator fun invoke(): Flow<List<CustomMatch>> =
        repository.customMatches
}