package com.example.androidproject.core.domain

data class CustomMatch(
    val id: Long,
    val startTimestamp: Long,
    val winnerCode: Int,
    val homePlayerId: Int,
    val homePlayerName: String,
    val awayPlayerId: Int,
    val awayPlayerName: String,
    val homeScores: List<Int>,
    val awayScores: List<Int>,
    val tournamentName: String
)