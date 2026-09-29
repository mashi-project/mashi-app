package com.serhij.mashi.data.remote.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetadataDto(
    @SerialName("assets")
    val assets: List<Asset>,
    @SerialName("attributes")
    val attributes: List<Attribute>,
    @SerialName("description")
    val description: String?,
    @SerialName("image")
    val image: String,
    @SerialName("name")
    val name: String
) {
    @Serializable
    data class Asset(
        @SerialName("label")
        val label: String,
        @SerialName("type")
        val type: String,
        @SerialName("uri")
        val uri: String
    )

    @Serializable
    data class Attribute(
        @SerialName("trait_type")
        val traitType: String?,
        @SerialName("value")
        val value: String?
    )
}