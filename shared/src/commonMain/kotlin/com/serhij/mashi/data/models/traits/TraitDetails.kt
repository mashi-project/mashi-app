package com.serhij.mashi.data.models.traits

import com.serhij.mashi.data.models.image.ImageType
import kotlinx.serialization.Serializable

@Serializable
data class TraitDetails(
    val type: TraitType,
    val url: String? = null,
    val imageType: ImageType? = null
)