package com.example.androidproject.ui.tennis
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidproject.R
import com.example.androidproject.core.domain.ToolbarUiData
import com.example.androidproject.core.usecases.authentication.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {

    private val _toolbarState = MutableLiveData<ToolbarUiData>()
    val toolbarState: LiveData<ToolbarUiData> get() = _toolbarState

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> get() = _searchQuery

    fun updateToolbarState(destinationId: Int) {
        _toolbarState.value = when (destinationId) {
            R.id.players_page_fragment -> ToolbarUiData(R.string.players_page, false)
            R.id.custom_matches_fragment -> ToolbarUiData(R.string.matches_page, false)
            else -> ToolbarUiData(R.string.player_content, true)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun logoutUser() {
        logoutUserUseCase()
    }
}