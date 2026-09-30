package com.serhij.mashi.data.remote.dtos

import com.serhij.mashi.data.models.save.MashupData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveMashupRes(
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String? = null,
    @SerialName("data") val data: MashupData? = null
)