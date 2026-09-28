package com.serhij.mashi.data.remote.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MashupDto(
    @SerialName("assets") val assets: List<Asset>,
    @SerialName("colors") val colors: Colors,
    @SerialName("count") val count: Int,
    @SerialName("id") val id: String,
    @SerialName("timestamp") val timestamp: Long,
    @SerialName("wallet") val wallet: String
) {
    @Serializable
    data class Asset(
        @SerialName("image") val image: String,
        @SerialName("name") val name: String
    )

    @Serializable
    data class Colors(
        @SerialName("base") val base: String,
        @SerialName("eyes") val eyes: String,
        @SerialName("hair") val hair: String
    )
}