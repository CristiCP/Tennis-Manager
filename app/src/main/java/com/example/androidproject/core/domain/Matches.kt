package com.example.androidproject.core.domain

data class Matches(
    val events: List<Match>
)

data class Match(
    val startTimestamp: Long,
    val winnerCode: Int,
    val homeTeam: Player,
    val awayTeam: Player,
    val homeScore: Score,
    val awayScore: Score,
    val tournament: Tournament
)

data class Score(
    val current: Int
)

data class Tournament(
    val name: String,
    val uniqueTournament: UniqueTournament?
)

data class UniqueTournament(
    val name: String,
    val id: Int,
    val groundType: String?
)
