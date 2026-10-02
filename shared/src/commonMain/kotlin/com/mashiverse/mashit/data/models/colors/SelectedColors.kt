package com.mashiverse.mashit.data.models.colors

import kotlinx.serialization.Serializable

@Serializable
data class SelectedColors(
    val base: String = "#00FF00",
    val eyes: String = "#FFFF00",
    val hair: String = "#0000FF"
)