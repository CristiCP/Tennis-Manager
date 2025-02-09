package com.example.androidproject.core.domain

data class MediaResponse(
    val media: List<Media>
)

data class Media(
    val title: String,
    val subtitle: String,
    val url: String,
    val thumbnailUrl: String
)