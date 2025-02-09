package com.example.androidproject.core.domain

sealed class TennisItem {
    data class PlayerItem(
        val player: PlayerRanking
    ) : TennisItem()

    data object LoadingItem : TennisItem()
}