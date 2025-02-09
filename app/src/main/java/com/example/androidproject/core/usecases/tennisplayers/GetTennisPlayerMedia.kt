package com.example.androidproject.core.usecases.tennisplayers

import com.example.androidproject.core.TennisPlayersRepository
import com.example.androidproject.core.domain.Media
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTennisPlayerMedia @Inject constructor(private val repository: TennisPlayersRepository) {

    operator fun invoke(playerId: Int): Flow<Media> =
        repository.getTennisPlayerMedia(playerId)
}