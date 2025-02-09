package com.example.androidproject.core.usecases.tennisplayers

import com.example.androidproject.core.TennisPlayersRepository
import com.example.androidproject.core.domain.Rankings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTennisPlayerRankingUseCase @Inject constructor(private val repository: TennisPlayersRepository) {

    operator fun invoke(playerId: Int): Flow<Rankings> =
        repository.getTennisPlayerRanking(playerId)
}