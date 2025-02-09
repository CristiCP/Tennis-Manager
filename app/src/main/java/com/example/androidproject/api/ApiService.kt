package com.example.androidproject.api

import com.example.androidproject.core.domain.Matches
import com.example.androidproject.core.domain.MediaResponse
import com.example.androidproject.core.domain.PlayerData
import com.example.androidproject.core.domain.Rankings
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/v1/rankings/type/5")
    suspend fun getTennisPlayer(): Rankings

    @GET("/api/v1/team/{id}")
    suspend fun getTennisPlayerById(@Path("id") postId: Int): PlayerData

    @GET("/api/v1/team/{id}/performance")
    suspend fun getTennisPlayerMatches(@Path("id") postId: Int): Matches

    @GET("/api/v1/team/{id}/media")
    suspend fun getTennisPlayerMedia(@Path("id") postId: Int): MediaResponse

    @GET("/api/v1/team/{id}/rankings")
    suspend fun getTennisPlayerRank(@Path("id") postId: Int): Rankings
}
