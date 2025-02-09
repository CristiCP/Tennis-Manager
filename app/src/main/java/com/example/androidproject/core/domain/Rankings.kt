package com.example.androidproject.core.domain

import com.google.gson.annotations.SerializedName

data class Rankings(
    val rankings: List<PlayerRanking>,
)

data class PlayerRanking(
    @SerializedName("team")
    val player: Player,
    val ranking: Int,
    val points: Int,
    val bestRanking: Int
)

data class Player(
    val name: String,
    val id: Int,
    val ranking: Int,
    val country: Country?,
)

data class Country(
    val alpha2: String,
    val name: String,
)
