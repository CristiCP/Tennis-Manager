package com.example.androidproject.core

import com.example.androidproject.core.domain.CustomMatch
import com.example.androidproject.core.domain.Match
import com.example.androidproject.core.domain.Media
import com.example.androidproject.core.domain.PlayerData
import com.example.androidproject.core.domain.PlayerRanking
import com.example.androidproject.core.domain.Rankings
import kotlinx.coroutines.flow.Flow

interface TennisPlayersRepository {

    val tennisPlayerList: Flow<List<PlayerRanking>>
    fun getPlayerById(id: Int): Flow<PlayerData>
    fun getPlayerMatches(id: Int): Flow<List<Match>>
    fun getPlayerLastMatch(id: Int): Flow<Match>
    fun addCustomMatch(match: CustomMatch): Flow<Boolean>
    val customMatches: Flow<List<CustomMatch>>
    fun getTennisPlayerMedia(id: Int): Flow<Media>
    fun getTennisPlayerRanking(id: Int): Flow<Rankings>
}