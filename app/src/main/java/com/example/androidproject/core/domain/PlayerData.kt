package com.example.androidproject.core.domain

import com.google.gson.annotations.SerializedName

data class PlayerData(
    @SerializedName("team")
    val player: PlayerDetails
)

data class PlayerDetails(
    val id: Int,
    val name: String,
    @SerializedName("playerTeamInfo")
    val playerInfo: PlayerInfo,
    val nameCode: String,
    val ranking: Int,
    val fullName: String,
    val country: Country?
)

data class PlayerInfo(
    val id: Int,
    val height: Double,
    val plays: String,
    val prizeCurrent: Int,
    val prizeTotal: Int,
    val birthDateTimestamp: Long,
)
