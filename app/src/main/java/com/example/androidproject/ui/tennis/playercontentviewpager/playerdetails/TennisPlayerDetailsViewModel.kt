package com.example.androidproject.ui.tennis.playercontentviewpager.playerdetails

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.R
import com.example.androidproject.core.domain.Match
import com.example.androidproject.core.domain.Media
import com.example.androidproject.core.domain.PlayerData
import com.example.androidproject.core.domain.Rankings
import com.example.androidproject.core.usecases.tennisplayers.GetTennisPlayerLastMatchUseCase
import com.example.androidproject.core.usecases.tennisplayers.GetTennisPlayerMedia
import com.example.androidproject.core.usecases.tennisplayers.GetTennisPlayerRankingUseCase
import com.example.androidproject.core.usecases.tennisplayers.GetTennisPlayerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TennisPlayerDetailsViewModel @Inject constructor(
    private val getTennisPlayerUseCase: GetTennisPlayerUseCase,
    private val getTennisPlayerMediaUseCase: GetTennisPlayerMedia,
    private val getTennisPlayerRankingUseCase: GetTennisPlayerRankingUseCase,
    private val getTennisPlayerLastMatchUseCase: GetTennisPlayerLastMatchUseCase
) : ViewModel() {

    private val _player = MutableLiveData<PlayerData>()
    val player: LiveData<PlayerData> get() = _player

    private val _media = MutableLiveData<Media>()
    val media: LiveData<Media> get() = _media

    private val _lastMatch = MutableLiveData<Match>()
    val lastMatch: LiveData<Match> get() = _lastMatch

    private val _homePlayer = MutableLiveData<PlayerData>()
    val homePlayer: LiveData<PlayerData> get() = _homePlayer

    private val _awayPlayer = MutableLiveData<PlayerData>()
    val awayPlayer: LiveData<PlayerData> get() = _awayPlayer

    private val _highestRank = MutableLiveData<Rankings>()
    val highestRank: LiveData<Rankings> get() = _highestRank

    private val _loadingState = MutableLiveData(true)
    val loadingState: LiveData<Boolean> get() = _loadingState

    fun getPlayer(playerId: Int) {
        viewModelScope.launch {
            getTennisPlayerUseCase(playerId).collect { player ->
                _player.value = player
            }
        }
    }

    fun getHomePlayer(playerId: Int) {
        viewModelScope.launch {
            getTennisPlayerUseCase(playerId).collect { player ->
                _homePlayer.value = player
            }
        }
    }

    fun getAwayPlayer(playerId: Int) {
        viewModelScope.launch {
            getTennisPlayerUseCase(playerId).collect { player ->
                _awayPlayer.value = player
            }
        }
    }

    fun getPlayerMedia(playerId: Int) {
        viewModelScope.launch {
            getTennisPlayerMediaUseCase(playerId).collect { media ->
                _media.value = media
            }
        }
    }

    fun getPlayerLastMatch(playerId: Int) {
        viewModelScope.launch {
            getTennisPlayerLastMatchUseCase(playerId).collect { match ->
                _lastMatch.value = match
            }
        }
    }

    fun getPlayerHighestRank(playerId: Int) {
        viewModelScope.launch {
            getTennisPlayerRankingUseCase(playerId).collect { rank ->
                _highestRank.value = rank
            }
        }
    }

    fun formatAge(birthDateTimestamp: Long): Int {
        val birthDate =
            Calendar.getInstance().apply { timeInMillis = birthDateTimestamp * TIME_MULTIPLIER }
        val today = Calendar.getInstance()
        val age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        return age
    }

    fun formatMoney(context: Context, amount: Int): String =
        when {
            amount >= AMOUNT_MULTIPLIER_MILLIONS -> String.format(
                Locale.US,
                context.getString(R.string.millions_format),
                amount / AMOUNT_MULTIPLIER_MILLIONS
            )

            amount >= AMOUNT_MULTIPLIER_THOUSANDS -> String.format(
                Locale.US,
                context.getString(R.string.thousands_format),
                amount / AMOUNT_MULTIPLIER_THOUSANDS
            )

            else -> amount.toString()
        }

    fun extractVideoId(url: String): String {
        val regex = Regex("(?<=v=|vi/|v/|youtu.be/|embed/)[^#&?]*")
        return regex.find(url)?.value ?: ""
    }

    fun loadingTrigger() {
        _loadingState.value = false
    }

    companion object {
        private const val AMOUNT_MULTIPLIER_MILLIONS: Double = 1_000_000.00
        private const val AMOUNT_MULTIPLIER_THOUSANDS: Double = 1_000.00
        private const val TIME_MULTIPLIER = 1_000
    }
}