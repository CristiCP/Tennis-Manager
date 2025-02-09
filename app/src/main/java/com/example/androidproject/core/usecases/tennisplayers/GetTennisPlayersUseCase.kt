package com.example.androidproject.core.usecases.tennisplayers

import com.example.androidproject.core.TennisPlayersRepository
import com.example.androidproject.core.domain.PlayerRanking
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTennisPlayersUseCase @Inject constructor(private val repository: TennisPlayersRepository) {

    operator fun invoke(): Flow<List<PlayerRanking>> = repository.tennisPlayerList
}