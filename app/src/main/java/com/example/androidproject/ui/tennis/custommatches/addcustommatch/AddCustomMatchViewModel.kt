package com.example.androidproject.ui.tennis.custommatches.addcustommatch

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.R
import com.example.androidproject.core.domain.PlayerRanking
import com.example.androidproject.core.usecases.tennisplayers.AddCustomMatchUseCase
import com.example.androidproject.core.usecases.tennisplayers.GetTennisPlayersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCustomMatchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getTennisPlayersUseCase: GetTennisPlayersUseCase,
    private val addCustomMatchUseCase: AddCustomMatchUseCase
) : ViewModel() {

    private val _players = MutableLiveData<List<PlayerRanking>>()
    val players: LiveData<List<PlayerRanking>> get() = _players

    init {
        loadPlayers()
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            getTennisPlayersUseCase().collect { emittedPlayers ->
                val playerList = emittedPlayers.map { it }
                    .sortedBy { playerRanking -> playerRanking.player.name }
                _players.value = playerList
            }
        }
    }

    fun validateAndAddMatch(
        homePlayer: PlayerRanking?,
        awayPlayer: PlayerRanking?,
        tournamentName: String,
        homeScores: List<Int>,
        awayScores: List<Int>,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ): String? {
        val validationError =
            validateInputs(homePlayer, awayPlayer, tournamentName, homeScores, awayScores)
        if (validationError != null) {
            return validationError
        }
        addMatch(
            homePlayer!!,
            awayPlayer!!,
            tournamentName,
            homeScores,
            awayScores,
            onSuccess,
            onFailure
        )
        return null
    }

    private fun validateInputs(
        homePlayer: PlayerRanking?,
        awayPlayer: PlayerRanking?,
        tournamentName: String,
        homeScores: List<Int>,
        awayScores: List<Int>
    ): String? = when {
        homePlayer == null -> context.getString(R.string.message_select_home_player)
        awayPlayer == null -> context.getString(R.string.message_select_away_player)
        homePlayer == awayPlayer -> context.getString(R.string.message_same_player)
        tournamentName.isBlank() -> context.getString(R.string.message_empty_tournament)
        !areSetScoresValid(
            homeScores,
            awayScores
        ) -> context.getString(R.string.message_invalid_score)

        else -> null
    }

    private fun areSetScoresValid(homeScores: List<Int>, awayScores: List<Int>): Boolean {
        val homeWins = homeScores.zip(awayScores).count { (home, away) -> home == 6 && home > away }
        val awayWins = homeScores.zip(awayScores).count { (home, away) -> away == 6 && away > home }
        return (homeWins == 2 && awayWins <= 1) || (awayWins == 2 && homeWins <= 1)
    }

    private fun addMatch(
        homePlayer: PlayerRanking,
        awayPlayer: PlayerRanking,
        tournamentName: String,
        homeScores: List<Int>,
        awayScores: List<Int>,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            addCustomMatchUseCase(
                homePlayer,
                awayPlayer,
                tournamentName,
                homeScores,
                awayScores
            ).collect { success ->
                if (success) {
                    onSuccess(context.getString(R.string.match_added_successfully))
                } else {
                    onFailure(context.getString(R.string.message_error_adding_match))
                }
            }
        }
    }
}