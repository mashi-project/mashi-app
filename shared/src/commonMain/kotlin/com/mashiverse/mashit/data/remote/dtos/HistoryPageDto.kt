package com.mashiverse.mashit.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class HistoryPageDto(
    val items: List<HistoryItemResponse>,
    val hasNextPage: Boolean
)

@Serializable
data class HistoryItemResponse(
    val id: String,
    val wallet: String,
    val imageUrl: String,
    val timestamp: String,
    val imageType: String
)