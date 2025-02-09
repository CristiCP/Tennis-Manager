package com.example.androidproject.ui.tennis.custommatches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.core.domain.CustomMatch
import com.example.androidproject.core.usecases.tennisplayers.GetCustomMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CustomMatchesViewModel @Inject constructor(
    private val getCustomMatchesUseCase: GetCustomMatchesUseCase
) : ViewModel() {

    private val _matches = MutableLiveData<List<CustomMatch>>()
    val matches: LiveData<List<CustomMatch>> get() = _matches

    private val _loadingState = MutableLiveData(true)
    val loadingState: LiveData<Boolean> get() = _loadingState

    init {
        loadCustomMatches()
    }

    fun loadCustomMatches() {
        viewModelScope.launch {
            getCustomMatchesUseCase().collect { matches ->
                _matches.value = matches
            }
        }
        _loadingState.value = false
    }

    fun filterMatchesByDate(date: Date) {
        _loadingState.value = true
        viewModelScope.launch {
            getCustomMatchesUseCase().collect { matches ->
                val filteredMatches = matches.filter { match ->
                    val matchDate = Date(match.startTimestamp)
                    isSameDay(matchDate, date)
                }
                _matches.value = filteredMatches
                _loadingState.value = false
            }
        }
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val calendar1 = Calendar.getInstance().apply { time = date1 }
        val calendar2 = Calendar.getInstance().apply { time = date2 }

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    fun filterMatches(query: String) = _matches.value?.filter { match ->
        match.homePlayerName.contains(query, ignoreCase = true) || match.awayPlayerName.contains(
            query,
            ignoreCase = true
        )
    }
}
