package com.example.androidproject.core.usecases.tennisplayers

import com.example.androidproject.core.TennisPlayersRepository
import com.example.androidproject.core.domain.Match
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTennisPlayerLastMatchUseCase @Inject constructor(private val repository: TennisPlayersRepository) {

    operator fun invoke(playerId: Int): Flow<Match> =
        repository.getPlayerLastMatch(playerId)
}