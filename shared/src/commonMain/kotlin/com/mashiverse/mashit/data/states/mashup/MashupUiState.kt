package com.mashiverse.mashit.data.states.mashup

data class MashupUiState(
    val isColorChange: Boolean = false,
    val isPreview: Boolean = false,
    val isCollectibles: Boolean = true,
    val isCollectionReady: Boolean = false
)