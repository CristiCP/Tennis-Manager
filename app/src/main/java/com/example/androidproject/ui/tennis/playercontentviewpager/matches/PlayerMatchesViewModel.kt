package com.example.androidproject.ui.tennis.playercontentviewpager.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.core.domain.Match
import com.example.androidproject.core.usecases.tennisplayers.GetTennisPlayerMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerMatchesViewModel @Inject constructor(
    private val getTennisPlayerMatchesUseCase: GetTennisPlayerMatchesUseCase
) : ViewModel() {

    private val _matches = MutableLiveData<List<Match>>()
    val matches: LiveData<List<Match>> get() = _matches

    private val _loadingState = MutableLiveData(true)
    val loadingState: LiveData<Boolean> get() = _loadingState

    fun loadPlayerMatches(playerId: Int) {
        viewModelScope.launch {
            getTennisPlayerMatchesUseCase(playerId).collect { match ->
                _matches.value = match
            }
            _loadingState.value = false
        }
    }
}