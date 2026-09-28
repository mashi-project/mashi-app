package com.serhij.mashi.data.models.save

import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
data class SaveMashupReq(
    val walletAddress: String,
    val mashup: MashupData
)

@Serializable
data class MashupData(
    val id: String = generateDraftId(),
    val ts: Long = Clock.System.now().toEpochMilliseconds(),
    val createdAt: String = Clock.System.now()
        .toString(), // Generates ISO-8601 string automatically
    val colors: MashupColors,
    val layers: List<MashupLayer>
)

@Serializable
data class MashupColors(
    val base: String,
    val eyes: String,
    val hair: String
)

@Serializable
data class MashupLayer(
    val name: String,
    val image: String
)

private fun generateDraftId(): String {
    return "draft_${Clock.System.now().toEpochMilliseconds()}"
}