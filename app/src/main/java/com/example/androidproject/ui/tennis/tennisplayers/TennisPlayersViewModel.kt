package com.example.androidproject.ui.tennis.tennisplayers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.core.domain.TennisItem
import com.example.androidproject.core.usecases.tennisplayers.GetTennisPlayersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TennisPlayersViewModel @Inject constructor(
    private val getTennisPlayersUseCase: GetTennisPlayersUseCase
) : ViewModel() {

    private val _players = MutableLiveData<List<TennisItem>>(listOf(TennisItem.LoadingItem))
    val players: LiveData<List<TennisItem>> get() = _players

    init {
        viewModelScope.launch {
            loadPlayers()
        }
    }

    private suspend fun loadPlayers() {
        getTennisPlayersUseCase().collect { emittedPlayers ->
            _players.value = _players.value?.dropLast(1)
                ?.plus(emittedPlayers.map { TennisItem.PlayerItem(it) })
                ?.plus(TennisItem.LoadingItem)
        }
        _players.value = _players.value?.dropLast(1)
    }

    fun filterPlayers(query: String) = _players.value?.filter { playerItem ->
        if (playerItem is TennisItem.PlayerItem) {
            playerItem.player.player.name.contains(query, ignoreCase = true)
        } else true
    }
}
