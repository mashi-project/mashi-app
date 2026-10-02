package com.mashiverse.mashit.data.models.mashi

import kotlinx.serialization.Serializable

@Serializable
data class Owned(
    val mint: Int,
    val timestamp: String
)
