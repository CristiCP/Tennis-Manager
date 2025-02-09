package com.example.androidproject.ui.tennis.playercontentviewpager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.core.domain.PlayerData
import com.example.androidproject.core.usecases.tennisplayers.GetTennisPlayerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TennisPlayerViewPagerViewModel @Inject constructor(
    private val getTennisPlayerUseCase: GetTennisPlayerUseCase
) : ViewModel() {

    private val _player = MutableLiveData<PlayerData>()
    val player: LiveData<PlayerData> get() = _player

    fun setPlayer(playerId: Int) {
        viewModelScope.launch {
            getTennisPlayerUseCase(playerId).collect { player ->
                _player.value = player
            }
        }
    }
}