package com.example.androidproject.core.usecases.tennisplayers

import com.example.androidproject.core.TennisPlayersRepository
import com.example.androidproject.core.domain.CustomMatch
import com.example.androidproject.core.domain.PlayerRanking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddCustomMatchUseCase @Inject constructor(private val repository: TennisPlayersRepository) {

    operator fun invoke(
        homePlayer: PlayerRanking,
        awayPlayer: PlayerRanking,
        tournamentName: String,
        homeScores: List<Int>,
        awayScores: List<Int>
    ): Flow<Boolean> = flow {
        val match = CustomMatch(
            id = System.currentTimeMillis(),
            startTimestamp = System.currentTimeMillis(),
            winnerCode = if (homeScores.sumOf { it } > awayScores.sumOf { it }) 1 else 2,
            homePlayerName = homePlayer.player.name,
            homePlayerId = homePlayer.player.id,
            awayPlayerName = awayPlayer.player.name,
            awayPlayerId = awayPlayer.player.id,
            homeScores = homeScores,
            awayScores = awayScores,
            tournamentName = tournamentName
        )
        repository.addCustomMatch(match).collect { result ->
            emit(result)
        }
    }
}