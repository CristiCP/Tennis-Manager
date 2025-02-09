package com.example.androidproject.infra

import android.content.Context
import android.content.SharedPreferences
import com.example.androidproject.api.ApiService
import com.example.androidproject.core.TennisPlayersRepository
import com.example.androidproject.core.domain.CustomMatch
import com.example.androidproject.core.domain.Match
import com.example.androidproject.core.domain.Media
import com.example.androidproject.core.domain.PlayerData
import com.example.androidproject.core.domain.PlayerRanking
import com.example.androidproject.core.domain.Rankings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TennisPlayersRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val apiService: ApiService
) : TennisPlayersRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(FILE_NAME_MATCHES, Context.MODE_PRIVATE)

    override val tennisPlayerList: Flow<List<PlayerRanking>> = flow {
        try {
            emit(apiService.getTennisPlayer().rankings.map { it })
        } catch (_: Exception) {
            //no-op
        }
    }

    override fun getPlayerById(id: Int): Flow<PlayerData> = flow {
        try {
            emit(apiService.getTennisPlayerById(id))
        } catch (_: Exception) {
            //no-op
        }
    }

    override fun getPlayerMatches(id: Int): Flow<List<Match>> = flow {
        try {
            emit(apiService.getTennisPlayerMatches(id).events.reversed())
        } catch (_: Exception) {
            //no-op
        }
    }

    override fun getPlayerLastMatch(id: Int): Flow<Match> = flow {
        try {
            emit(apiService.getTennisPlayerMatches(id).events.last())
        } catch (_: Exception) {
            //no-op
        }
    }

    override val customMatches: Flow<List<CustomMatch>> = flow {
        try {
            emit(sharedPreferences.getString(KEY_CUSTOM_MATCHES, "").orEmpty().lines()
                .filter { it.isNotBlank() }
                .map { deserializeMatch(it) })
        } catch (_: Exception) {
            //no-op
        }
    }

    override fun getTennisPlayerMedia(id: Int): Flow<Media> = flow {
        try {
            emit(apiService.getTennisPlayerMedia(id).media.first())
        } catch (_: Exception) {
            //no-op
        }
    }

    override fun getTennisPlayerRanking(id: Int): Flow<Rankings> = flow {
        try {
            emit(apiService.getTennisPlayerRank(id))
        } catch (_: Exception) {
            //no-op
        }
    }

    override fun addCustomMatch(match: CustomMatch): Flow<Boolean> = flow {
        try {
            val matchesData = sharedPreferences.getString(KEY_CUSTOM_MATCHES, "").orEmpty()
            val serializedMatch = serializeMatch(match)
            val updatedMatchesData = if (matchesData.isEmpty()) {
                serializedMatch
            } else {
                "$matchesData\n$serializedMatch"
            }
            val isSuccessful =
                sharedPreferences.edit().putString(KEY_CUSTOM_MATCHES, updatedMatchesData).commit()
            emit(isSuccessful)
        } catch (_: Exception) {
            emit(false)
        }
    }

    private fun serializeMatch(match: CustomMatch): String = listOf(
        match.startTimestamp.toString(),
        match.winnerCode.toString(),
        match.homePlayerName,
        match.homePlayerId.toString(),
        match.awayPlayerName,
        match.awayPlayerId.toString(),
        match.homeScores.joinToString(";") { it.toString() },
        match.awayScores.joinToString(";") { it.toString() },
        match.tournamentName
    ).joinToString(",")


    private fun deserializeMatch(serializedMatch: String): CustomMatch {
        val parts = serializedMatch.split(",")
        return CustomMatch(
            id = parts[0].toLong(),
            startTimestamp = parts[0].toLong(),
            winnerCode = parts[1].toInt(),
            homePlayerName = parts[2],
            homePlayerId = parts[3].toInt(),
            awayPlayerName = parts[4],
            awayPlayerId = parts[5].toInt(),
            homeScores = parts[6].split(";").map { it.toInt() },
            awayScores = parts[7].split(";").map { it.toInt() },
            tournamentName = parts[8]
        )
    }

    companion object {
        private const val FILE_NAME_MATCHES = "CustomMatches"
        private const val KEY_CUSTOM_MATCHES = "custom_matches"
    }
}