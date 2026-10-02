package com.mashiverse.mashit.data.remote.dtos

import com.mashiverse.mashit.data.models.save.MashupData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveMashupRes(
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String? = null,
    @SerialName("data") val data: MashupData? = null
)